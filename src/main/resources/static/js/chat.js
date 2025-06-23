let fromUser = document.querySelector("div#fromUser").innerText;
let toUser = document.querySelector("div#toUser").innerText
let socket = new WebSocket("ws://localhost:8080/websocket/" + fromUser);
const sendBtn = document.getElementById("send");
const imgBtn = document.getElementById("img");

document.addEventListener("DOMContentLoaded", function () {
    axios.post("/chat/history", {
        "fromUser": fromUser,
        "toUser": toUser
    }).then(({data})=>{
        console.log(data);
        data.forEach((mes)=>{
            mes.MFromUser = mes.MFromUser === fromUser ? '我' : toUser;
            mes.isRead = true;
            addMessageElement(mes);
        })
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

function addMessageElement(message) {
    const messageElement = document.createElement('div');
    const messageFrom = document.createElement('span');
    const messageTime = document.createElement('span');
    const messageContent = document.createElement('span');
    const messageIsRead = document.createElement('span');
    messageIsRead.id = "isRead";
    messageFrom.innerText = `${message.MFromUser}: `;
    messageTime.innerText = ` ${message.MTime}`;
    messageContent.innerText = `${message.MContent}`;
    messageIsRead.innerText = message.isRead === true ? ' 已读' : ' 未读';

    messageElement.appendChild(messageFrom);
    if (message.isImg) {
        const img = document.createElement('img');
        img.src = message.MContent;
        img.style.width = "300px";
        img.style.height = "150px";
        img.style.objectFit = "cover";
        messageElement.appendChild(img);
    } else {
        messageElement.appendChild(messageContent);
    }
    messageElement.appendChild(messageTime);
    messageElement.appendChild(messageIsRead);
    document.getElementById("chatBox").appendChild(messageElement);
}

