document.addEventListener("DOMContentLoaded", function() {
    const chatForm = document.getElementById("chat-form");
    const chatInput = document.getElementById("prompt");
    const messagesContainer = document.querySelector(".direct-chat-messages");
    const sendButton = chatForm.querySelector(".btn-primary");
    const pathParts = window.location.pathname.split("/");
    const chatId = pathParts[pathParts.length - 1];

    if (!chatForm || !chatInput || !messagesContainer) return;


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

        // 2. Создаем блок для ответа AI
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

            while (true) {
                const { done, value } = await reader.read();
                if (done) break;

                const chunk = decoder.decode(value, { stream: true });

                const lines = chunk.split('\n');
                for (let line of lines) {
                    if (line.startsWith('data:')) {
                        let dataStr = line.substring(5);
                        if (dataStr) {
                            let token = dataStr;

                            try {
                                const parsed = JSON.parse(dataStr);
                                if (parsed.text) token = parsed.text;
                            } catch (e) {
                                console.log(e)
                            }

                            fullText += token;
                            aiBubble.innerHTML = marked.parse(fullText);
                            messagesContainer.scrollTop = messagesContainer.scrollHeight;
                        }
                    }
                }
            }
        } catch (error) {
            console.error("Ошибка:", error);
            aiBubble.innerHTML = `<span class="text-danger">Ошибка при получении ответа</span>`;
        }
    });
});
