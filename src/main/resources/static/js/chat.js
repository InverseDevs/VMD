//'use strict';
//var chatPage = document.querySelector('#chat-page');
//var messageForm = document.querySelector('#messageForm');
//var messageInput = document.querySelector('#message');
//var messageArea = document.querySelector('#messageArea');
//var connectingElement = document.querySelector('.connecting');
//var stompClient = null;
//
//function connect() {
//    var socket = new SockJS('/ws');
//    stompClient = Stomp.over(socket);
//    stompClient.connect({}, onConnected, onError);
//}
//function onConnected() {
//    stompClient.subscribe('/chat/topic/public', onMessageReceived);
//}
//function onError(error) {
//    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
//    connectingElement.style.color = 'red';
//}
//function sendMessage(event) {
//    var messageContent = messageInput.value.trim();
//    if(messageContent && stompClient) {
//        var chatMessage = {
//            sender: sender,
//            content: messageInput.value,
//            type: 'CHAT',
//            receiver: receiver
//        };
//        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
//        messageInput.value = '';
//    }
//    event.preventDefault();
//}
//function onMessageReceived(payload) {
//    var message = JSON.parse(payload.body);
//    var messageElement = document.createElement('li');
//    messageElement.classList.add('chat-message');
//    var usernameElement = document.createElement('span');
//    var usernameText = document.createTextNode(message.sender);
//    usernameElement.appendChild(usernameText);
//    messageElement.appendChild(usernameElement);
//    var textElement = document.createElement('p');
//    var messageText = document.createTextNode(message.content);
//    textElement.appendChild(messageText);
//    messageElement.appendChild(textElement);
//    messageArea.appendChild(messageElement);
//    messageArea.scrollTop = messageArea.scrollHeight;
//}
//
//window.onload = function() {
//  connect();
//};
//
//messageForm.addEventListener('submit', sendMessage, true)

var ws;

function connect() {
	ws = new WebSocket('ws://localhost:8080/ws');
	ws.onmessage = function(data){
		onReceive(data.data);
	}
}

function disconnect() {
    if (ws != null) {
        ws.close();
    }
    console.log("Disconnected");
}

function startReceiving() {
    ws.send("start");
}

function stopReceiving() {
    ws.send("stop");
}

function onReceive(message) {
    console.log(" " + message + "");
}

window.onload = function() {
  connect();
};

var startButton = document.getElementById("startButton");
startButton.onclick = startReceiving;

var stopButton = document.getElementById("stopButton");
stopButton.onclick = stopReceiving;