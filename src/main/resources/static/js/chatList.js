document.querySelectorAll("div#productInfo").forEach(btn => btn.addEventListener("click", function () {
    let toUser = this.nextElementSibling.nextElementSibling.nextElementSibling.innerText;
    let fromUser = this.nextElementSibling.nextElementSibling.innerText;
    let socket = new WebSocket("ws://localhost:8080/websocket/" + fromUser);
    const chatBox = document.getElementById("chatBox");
    chatBox.style.display = "block";
    document.getElementById("msg").innerText = "";
    document.getElementById("noChatSelected").style.display = "none";
    document.getElementById("chatTitle").innerText = toUser;


    const sendBtn = document.getElementById("send");
    const imgBtn = document.getElementById("img");

    axios.post("/chat/history", {
        "fromUser": fromUser,
        "toUser": toUser
    }).then(({data})=>{
        console.log(data);
        data.forEach((mes)=>{
            mes.MFromUser = mes.MFromUser === fromUser ? '我' : toUser;
            if (mes.MToUser === fromUser) mes.isRead = true;
            addMessageElement(mes);
        })
    })

    socket.onopen = function (event) {
        console.log("连接成功");
        sendText(false, "", true);
    }

    socket.onmessage = function (event) {
        console.log("收到消息：" + event.data);
        let msg = JSON.parse(event.data);
        if (msg.isConfirmed && msg.MFromUser === toUser){
            document.querySelectorAll("span#isRead").forEach((e)=>{
                e.innerText = ' 已读';
            })
            return;
        }
        if (msg.MFromUser !== toUser)
            return;
        sendText(false, "", true);
        msg.isRead = true;
        addMessageElement(msg);
    }

    socket.onclose = function (ev) {
        console.log("连接关闭");
    }

    sendBtn.onclick = function () {
        let content = document.getElementById("msgContent").value;
        if (content === "")
            return;
        sendText(false, content, false);
        addMessageElement({
            "MFromUser": '我',
            "MToUser": toUser,
            "isImg": false,
            "MContent": content,
            "MTime": timestampToTime(new Date().getTime()),
            "isRead": false,
            "isConfirmed": false
        })
    }

    imgBtn.onchange = function () {
        let file = this.files[0];
        let formData = new FormData()
        formData.append('files', file)
        axios.post('/chat/uploadImg', formData).then(({data}) => {
            console.log(data);
            sendText(true, data, false);
        })
        addMessageElement({
            "MFromUser": '我',
            "MToUser": toUser,
            "isImg": true,
            "MContent": URL.createObjectURL(file),
            "MTime": timestampToTime(new Date().getTime()),
            "isRead": false,
            "isConfirmed": false
        })
    }


    function timestampToTime(timestamp) {
        let date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
        let Y = date.getFullYear() + '-';
        let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
        let D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
        let h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
        let m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
        let s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
        return Y + M + D + h + m + s;
    }

    function sendText(isImg, content, isConfirmed) {
        let msg = {
            "MFromUser": fromUser,
            "MToUser": toUser,
            "isImg": isImg,
            "MContent": content,
            "MTime": timestampToTime(new Date().getTime()),
            "isRead": false,
            "isConfirmed": isConfirmed
        }
        socket.send(JSON.stringify(msg));
    }

    // function addMessageElement(message) {
    //     const messageElement = document.createElement('div');
    //     const messageFrom = document.createElement('span');
    //     const messageTime = document.createElement('span');
    //     const messageContent = document.createElement('span');
    //     const messageIsRead = document.createElement('span');
    //     messageIsRead.id = "isRead";
    //     messageFrom.innerText = message.MFromUser === '我' ? ':我' : toUser + ":";
    //     messageTime.innerText = ` ${message.MTime}`;
    //     messageContent.innerText = `${message.MContent}`;
    //     messageIsRead.innerText = message.isRead === true ? ' 已读' : ' 未读';
    //     messageIsRead.style.opacity = "0.5";
    //     messageIsRead.style.color = messageIsRead.innerText === ' 已读' ? 'green' : 'red';
    //
    //     messageElement.appendChild(messageFrom);
    //     if (message.isImg) {
    //         const img = document.createElement('img');
    //         img.src = message.MContent;
    //         img.style.width = "300px";
    //         img.style.height = "150px";
    //         img.style.objectFit = "cover";
    //         messageElement.appendChild(img);
    //     } else {
    //         messageContent.style.display = "flex";
    //         messageContent.style.border = "1px solid #ccc";
    //         messageContent.style.margin = "10px";
    //         messageElement.appendChild(messageContent);
    //     }
    //     messageElement.appendChild(messageTime);
    //     messageElement.appendChild(messageIsRead);
    //     if (message.MFromUser === '我'){
    //         messageElement.style.textAlign = "right";
    //         messageElement.style.flexDirection = "row-reverse";
    //     }
    //     document.getElementById("msg").append(messageElement);
    // }

    function addMessageElement(message) {
        // 创建消息容器
        const messageElement = document.createElement('div');
        messageElement.classList.add('message');
        messageElement.classList.add(message.MFromUser === '我' ? 'sent' : 'received');

        // 创建消息内容容器
        const messageContent = document.createElement('div');
        messageContent.classList.add('message-content');

        // 创建消息元数据容器（发送者+时间）
        const messageMeta = document.createElement('div');
        messageMeta.classList.add('message-meta');

        // 创建发送者元素
        const messageFrom = document.createElement('span');
        messageFrom.classList.add('message-sender');
        messageFrom.innerText = message.MFromUser === '我' ? '我' : toUser;

        // 创建时间元素
        const messageTime = document.createElement('span');
        messageTime.classList.add('message-time');
        messageTime.innerText = message.MTime;

        // 创建已读状态元素
        const messageIsRead = document.createElement('span');
        messageIsRead.classList.add('message-status');
        messageIsRead.innerText = message.isRead ? '✓✓' : '✓';
        messageIsRead.style.color = message.isRead ? '#4CAF50' : '#9E9E9E';
        messageIsRead.title = message.isRead ? '已读' : '已发送';

        // 组装元数据
        messageMeta.appendChild(messageFrom);
        messageMeta.appendChild(messageTime);

        // 处理不同类型的内容
        if (message.isImg) {
            // 图片消息
            const imgContainer = document.createElement('div');
            imgContainer.classList.add('image-container');

            const img = document.createElement('img');
            img.src = message.MContent;
            img.classList.add('message-image');
            img.alt = '图片消息';

            // 添加点击预览功能
            img.addEventListener('click', function() {
                const overlay = document.createElement('div');
                overlay.classList.add('image-overlay');

                const fullImg = document.createElement('img');
                fullImg.src = this.src;
                fullImg.classList.add('full-image');

                overlay.appendChild(fullImg);
                document.body.appendChild(overlay);

                overlay.addEventListener('click', function() {
                    document.body.removeChild(this);
                });
            });

            imgContainer.appendChild(img);
            messageContent.appendChild(imgContainer);
        } else {
            // 文本消息
            const textElement = document.createElement('div');
            textElement.classList.add('message-text');
            textElement.innerText = message.MContent;
            messageContent.appendChild(textElement);
        }

        // 组装消息元素
        messageElement.appendChild(messageMeta);
        messageElement.appendChild(messageContent);
        messageElement.appendChild(messageIsRead);

        // 添加到消息容器
        const msgContainer = document.getElementById("msg");
        msgContainer.appendChild(messageElement);

        // 滚动到底部
        msgContainer.scrollTop = msgContainer.scrollHeight;

        // 添加消息进入动画
        setTimeout(() => {
            messageElement.style.opacity = "1";
            messageElement.style.transform = "translateY(0)";
        }, 10);
    }

}))