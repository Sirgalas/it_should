document.addEventListener("DOMContentLoaded", function() {
    const chatForm = document.getElementById("chat-form");
    const chatInput = document.getElementById("prompt");
    const messagesContainer = document.querySelector(".direct-chat-messages");
    const sendButton = chatForm.querySelector(".btn-primary");
    const pathParts = window.location.pathname.split("/");
    const chatId = pathParts[pathParts.length - 1];

    if (!chatForm || !chatInput || !messagesContainer) return;

    if (window.marked) {
        marked.setOptions({
            gfm: true,
            breaks: true
        });

        // Рендерим Markdown для всех ранее загруженных сообщений ассистента из базы
        document.querySelectorAll(".direct-chat-msg:not(.right) .direct-chat-text").forEach(bubble => {
            bubble.innerHTML = marked.parse(bubble.textContent);
        });
    }

    function extractToken(dataStr) {
        if (!dataStr) return "";
        if (dataStr.startsWith("{")) {
            try {
                const parsed = JSON.parse(dataStr);
                if (parsed && typeof parsed.text === "string") {
                    return parsed.text;
                }
            } catch (e) {
                let combined = "";
                const matches = dataStr.matchAll(/\{"text":"((?:\\.|[^"\\])*)"\}/g);
                for (const match of matches) {
                    try {
                        const parsed = JSON.parse(match[0]);
                        if (parsed && typeof parsed.text === "string") {
                            combined += parsed.text;
                        }
                    } catch (err) {}
                }
                return combined;
            }
            return "";
        }
        return dataStr;
    }

    sendButton.addEventListener("click", async function(event) {
        event.preventDefault();

        const prompt = chatInput.value;
        if (!prompt.trim()) return;

        chatInput.value = "";

        const userDiv = document.createElement("div");
        userDiv.className = "direct-chat-msg right";
        userDiv.innerHTML = `
            <div class="direct-chat-infos clearfix">
                <span class="direct-chat-name float-right">user</span>
            </div>
            <div class="direct-chat-text">${prompt}</div>
        `;
        messagesContainer.appendChild(userDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;

        const url = `/stream/chat/${chatId}`;

        // Создаем блок для ответа AI
        const aiDiv = document.createElement("div");
        aiDiv.className = "direct-chat-msg";
        aiDiv.innerHTML = `
            <div class="direct-chat-infos clearfix">
                <span class="direct-chat-name float-left">assistant</span>
            </div>
        `;

        const aiBubble = document.createElement("div");
        aiBubble.className = "direct-chat-text";
        aiBubble.innerHTML = `<i class="fa-solid fa-spinner fa-spin"></i>`; // Спиннер ожидания
        aiDiv.appendChild(aiBubble);
        messagesContainer.appendChild(aiDiv);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;

        let fullText = "";

        try {
            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ prompt: prompt })
            });

            if (!response.ok) {
                throw new Error("Сетевая ошибка");
            }

            const reader = response.body.getReader();
            const decoder = new TextDecoder("utf-8");

            aiBubble.innerHTML = "";

            let buffer = "";

            while (true) {
                const { done, value } = await reader.read();
                if (done) break;

                buffer += decoder.decode(value, { stream: true });

                const lines = buffer.split(/\r?\n/);
                // Оставляем незавершенную строку в буфере
                buffer = lines.pop();

                for (let line of lines) {
                    let trimmedLine = line.trim();
                    if (trimmedLine.startsWith("data:")) {
                        let dataStr = trimmedLine.substring(5).trim();
                        let token = extractToken(dataStr);
                        if (token) {
                            fullText += token;
                            if (window.marked) {
                                aiBubble.innerHTML = marked.parse(fullText);
                            } else {
                                aiBubble.textContent = fullText;
                            }
                            messagesContainer.scrollTop = messagesContainer.scrollHeight;
                        }
                    }
                }
            }

            if (buffer) {
                let trimmedLine = buffer.trim();
                if (trimmedLine.startsWith("data:")) {
                    let dataStr = trimmedLine.substring(5).trim();
                    let token = extractToken(dataStr);
                    if (token) {
                        fullText += token;
                        if (window.marked) {
                            aiBubble.innerHTML = marked.parse(fullText);
                        } else {
                            aiBubble.textContent = fullText;
                        }
                        messagesContainer.scrollTop = messagesContainer.scrollHeight;
                    }
                }
            }
        } catch (error) {
            console.error("Ошибка:", error);
            aiBubble.innerHTML = `<span class="text-danger">Ошибка при получении ответа</span>`;
        }
    });
});
