const url = 'http://localhost:8000';

var recipId;

var authorId;

var userName;

function AddMessage(data) {



    var clone = document.getElementById("ms").cloneNode(true);
    clone.setAttribute("class", "message");
    clone.removeAttribute("id");
    document.getElementById("scroll").appendChild(clone);

    clone.children[1].textContent = data.message;
    
    scrollDown();
}

function connect(user, author, recip) {
	authorId = author;
	recipId = recip;
	userName = user;
	
    console.log("connecting to chat...")
    let socket = new SockJS(url + '/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to: " + frame);
        stompClient.subscribe("/topic/messages/" + userName, function (response) {
            var data = JSON.parse(response.body);

            AddMessage(data);
            
        });
    });
    
    console.log(userName + " " + authorId + " " + recipId);
}

function disconnect(){
	stompClient.disconnect();
}
function sendMessage() {

    var inputText = document.getElementById('messageText');


    stompClient.send("/app/chat/" + userName, {}, JSON.stringify({

    	author: authorId,
    	
    	recipient: recipId,
    
        message: inputText.value,
        
        imageUrl: null
    }));
    
}
function OpenDialog(param)
{
	var id = param.getAttribute("value");
	
	
	document.location.href = "/Dialogs/person?contactId="+id; 

}

function OpenFilePanel()
{
	var panel = document.getElementById("FilePanel");
	
	panel.style.display = 'block';
	
}

function scrollDown() {
    var scroll = document.getElementById("scroll");
    scroll.scrollTop = scroll.scrollHeight;
}

function WriteMessage(param) {

    
    document.location.href = "/Dialogs/createContact?userId=" + param.value; 
    
}


function addComment(param) {

    


    let post = param.parentNode.parentNode;
    let x = param.parentNode.children[2];
    if (x.style.display === "none") {
        x.style.display = "block";
        post.style.height = "500px";
    } else {
        x.style.display = "none";
        post.style.height = "250px";
    }
}




function newsConnection()
{
	 let socket = new SockJS(url + '/news-comment');
	    stompClient = Stomp.over(socket);
	    stompClient.connect({}, function (frame) {
	        
	        
	    });
}

function likesConnection() {
    let socket = new SockJS(url + '/news-likes');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {


    });
}
function contactConnection() {
    let socket = new SockJS(url + '/contact');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {


    });
}

function sendAction(param, act) {

    stompClient.send("/app/contact", {}, JSON.stringify({

        action: act,

        contact: param.value,

        user: authorId
    }));
    param.parentNode.parentNode.parentNode.style.display = 'none';


}

function setLike(value, button) {

    isLiked = button.parentNode.value;
    
    if (!isLiked) {
        stompClient.send("/app/news-likes", {}, JSON.stringify({

            newsId: button.value,

            value: value,

            authorId: authorId
        }));

        var resultElement = button.parentNode.children[1];
        resultElement.textContent = Number.parseInt(resultElement.textContent) + value;
        button.parentNode.value = true;
    }
}

function setComment(param) {

    var value = param.parentNode.children[0].value;

    if (value.lenght == 0 || !value.trim())
        return;

    stompClient.send("/app/news-comment",  {}, JSON.stringify({

        author: authorId,

        myNew: param.value,

        text: value

    }));

    var clone = document.getElementById("example").cloneNode(true);

    param.parentNode.parentNode.children[0].appendChild(clone);
    clone.removeAttribute("id");
    clone.style.display = "block";
    clone.querySelector("a").textContent = "Вы";
    clone.querySelector(".row").textContent = value;

}

function setActive(id, closeId) {
    document.getElementById(id).style.display = 'block';
    document.getElementById(closeId).style.display = 'none';
}


function changePage(param) {
    document.location.href = "/Friends?type=" + param;
}

function openUserPanel(param) {
    document.location.href = "/send-request?userId=" + param.value;
}

function Block(param, action) {
    var link = "/block-user?userId=" + param.value;
    document.location.href = link + "&action=" + action;
}

function showNews(type) {
    document.location.href = "/News?type=" + type;

}