document.addEventListener("DOMContentLoaded", async function () {
    // 获取当前用户
    let fromUserAlert = document.getElementById("FromUser").innerText;
    fromUserAlert = fromUserAlert.slice(0, fromUserAlert.indexOf("@"));
    let alertSocket = new WebSocket("wss://syh0621.xyz/websocket/" + fromUserAlert);

    for (const user of document.querySelectorAll("#ToUser")) {
        await axios.post("/chat/unread", {
            "fromUser": user.previousElementSibling.innerText,
            "toUser": user.innerText
        }).then(({data}) => {
            if (data > 0) {
                user.parentElement.children[0].children[1].children[2].innerText = data;
                user.parentElement.children[0].children[1].children[2].style.display = "block";
            }
        })
        await axios.post("/chat/history", {
            "fromUser": user.previousElementSibling.innerText,
            "toUser": user.innerText
        }).then(({data}) => {
            console.log(data)
            const lastMes = data[data.length - 1];
            //最后一条信息
            user.parentElement.children[0].children[1].children[1].innerText = lastMes.MContent;
            if (lastMes.isImg) {
                user.parentElement.children[0].children[1].children[1].innerHTML = "[图片]";
            }
        })
    }

    alertSocket.onopen = function (event) {
        console.log("连接成功");
    }

    alertSocket.onmessage = function ({data}) {
        console.log("收到消息：" + data);
        // 这里的alertUser是发送消息的用户
        const alertUser = JSON.parse(data).MFromUser;
        console.log(alertUser);
        const alertUserEle = Array.from(document.querySelectorAll("p#ToUser")).find(ele => ele.textContent === `${alertUser}`);
        console.log(alertUserEle)
        if (alertUserEle) {
            alertUserEle.parentElement.children[0].children[1].children[2].innerText = parseInt(alertUserEle.parentElement.children[0].children[1].children[2].innerText) + 1;
            alertUserEle.parentElement.children[0].children[1].children[1].innerText = JSON.parse(data).MContent;
            alertUserEle.parentElement.children[0].children[1].children[2].style.display = "block";
        }
    }

    alertSocket.onerror = function (e) {
        console.log(e);
    }
})

document.querySelectorAll("div#productInfo").forEach(btn => btn.addEventListener("click", async function () {
    let toUser = this.nextElementSibling.nextElementSibling.nextElementSibling.innerText;
    let fromUser = this.nextElementSibling.nextElementSibling.innerText;
    let pid = this.nextElementSibling.innerText;
    let alertNumEle = this.parentElement.children[0].children[1].children[2];
    let lastMesEle = this.parentElement.children[0].children[1].children[1];
    let socket = new WebSocket("wss://syh0621.xyz/websocket/" + fromUser);
    const chatBox = document.getElementById("chatBox");
    chatBox.style.display = "block";
    document.getElementById("msg").innerText = "";
    document.getElementById("noChatSelected").style.display = "none";
    await axios.post(`/getUser?username=${toUser.substring(0, toUser.indexOf('@'))}`).then(({data}) => {
        console.log(data)
        document.getElementById("chatTitle").innerText = data.nickname;
    });

    socket.onerror = function (e) {
        console.log(e);
    }

    const sendBtn = document.getElementById("send");
    const imgBtn = document.getElementById("img");

    await axios.post("/chat/history", {
        "fromUser": fromUser,
        "toUser": toUser
    }).then(({data}) => {
        console.log(data);
        data.forEach((mes) => {
            mes.MFromUser = mes.MFromUser === fromUser ? '我' : toUser;
            if (mes.MToUser === fromUser) mes.isRead = true;
            addMessageElement(mes);
        })
    })

    socket.onopen = function (event) {
        console.log("连接成功");
        alertNumEle.innerText = 0;
        alertNumEle.style.display = "none";
        sendText(false, "", true);
    }

    socket.onmessage = function (event) {
        console.log("收到消息：" + event.data);
        let msg = JSON.parse(event.data);
        if (msg.isConfirmed && msg.MFromUser === toUser) {
            document.querySelectorAll("span#isRead").forEach((e) => {
                e.innerText = ' 已读';
            })
            return;
        }
        if (msg.MFromUser !== toUser)
            return;
        sendText(false, "", true);
        msg.isRead = true;
        addMessageElement(msg);
        lastMesEle.innerText = msg.MContent
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
        lastMesEle.innerText = content;
        document.getElementById("msgContent").value = "";
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

    async function addMessageElement(message) {
        let fromUser = message.MFromUser === '我' ? document.querySelector("p#fromUser").innerText : message.MFromUser;
        fromUser = fromUser.substring(0, fromUser.indexOf('@'));
        let fromUserPic = document.createElement('span');
        axios.post(`/getUser?username=${fromUser}`).then(({data}) => {
            fromUserPic.innerHTML = `<img src="${data.profilePicture}" alt="头像" class="avatar">`;
        })
        fromUserPic.addEventListener('click', function () {
            window.location.href = `/user/${fromUser}`;
        })

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
            img.addEventListener('click', function () {
                const overlay = document.createElement('div');
                overlay.classList.add('image-overlay');

                const fullImg = document.createElement('img');
                fullImg.src = this.src;
                fullImg.classList.add('full-image');

                overlay.appendChild(fullImg);
                document.body.appendChild(overlay);

                overlay.addEventListener('click', function () {
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
        let msg = document.createElement('div');
        msg.appendChild(fromUserPic);
        msg.appendChild(messageElement);
        msg.style.display = 'flex';
        msg.style.flexDirection = 'row';
        if (message.MFromUser === '我') {
            msg.style.flexDirection = 'row-reverse';
        }
        msgContainer.appendChild(msg);
        // msgContainer.appendChild(fromUserPic);
        // msgContainer.appendChild(messageElement);

        // 滚动到底部
        msgContainer.scrollTop = msgContainer.scrollHeight;

        // 添加消息进入动画
        setTimeout(() => {
            messageElement.style.opacity = "1";
            messageElement.style.transform = "translateY(0)";
        }, 10);

        // 滚动到最新消息
        document.getElementById("chatBox").scrollTo(0, document.getElementById("chatBox").scrollHeight);
    }

}))

document.querySelectorAll("div#productInfo").forEach(div => {
    div.addEventListener("contextmenu", function(e) {
        e.preventDefault();
        const menu = document.getElementById("contextMenu");
        menu.children[0].children[0].href = `/product/${this.nextElementSibling.innerText}`;
        menu.style.left = `${e.pageX}px`;
        menu.style.top = `${e.pageY}px`;
        menu.style.display = "block";
    });
    document.addEventListener('click', function() {
        document.getElementById("contextMenu").style.display = "none";
    })
})