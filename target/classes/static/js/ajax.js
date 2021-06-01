function ajaxCarList(elem) {
    var pattern = document.getElementById(elem).value;

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/ajax/author-name', true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");


    xhr.onreadystatechange = function() {
    	if (xhr.readyState != 4) 
    		return;
    	if (xhr.status != 200) {
    		alert(xhr.status + ': ' + xhr.statusText);
    	} else {
    		try {
    			var str = JSON.parse(xhr.responseText);
    			alert(str.text);
    			
    		} catch (e) {
    			alert('Error during request!');
    		}
    	}
    }
    xhr.send("q=" + pattern);    
	
 
}