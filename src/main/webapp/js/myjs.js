//const reportStatus = function(message) {
//	console.log(message);
//};
//const status = document.getElementById("status");
const reportStatus = function(message) {
	const status = document.getElementById("status");
//    status.innerHTML += `\${message} <br/>`;
    status.innerHTML += message+"<br/>";
    status.scrollTop = status.scrollHeight;
};
function upload() {
alert('!');
	const sasUrl = "https://imsstorage.blob.core.usgovcloudapi.net/videofiles?sp=r&st=2023-11-01T18:57:38Z&se=2023-11-02T02:57:38Z&spr=https&sv=2022-11-02&sr=c&sig=usR%2FMNsnp8ung6xOb%2F8heC7PbPIIYTmtVJiyTa0WEUM%3D" ;
	const containerURL = new azblob.ContainerURL(sasUrl,
	    							azblob.StorageURL.newPipeline(new azblob.AnonymousCredential));

	//download selected file 
	const proceedDownload = async () => {
	    try {
	    		let progEvent;
	    		const fileTitle = 'my title';
	    		const fileName = 'bigfile.mp4';
	    		const fileSize = '1,672,172,000';
	    		let startTime = new Date();
	            let lastRemained = 3600*4 //seconds
	           	
	            const blockBlobURL = azblob.BlockBlobURL.fromContainerURL(containerURL,fileName);
	          	//get the downloaded data - total size unknown from AMS
	            const downloadResponse = await blockBlobURL.download(azblob.Aborter.none, 0,undefined,
	            	{
	                  //progress: (progressEvent) => {
	                    //console.log((progressEvent.loadedBytes/1024/1000).toFixed(2)+'/'+fileSize);
	                  //	reportStatus((progressEvent.loadedBytes/1024/1000).toFixed(2));
	                  //}
	                  
	                  progress: (progressEvent) => {
	                    	console.log('Downloading '+fileName+' '+progressEvent.loadedBytes+ ' of '+ fileSize);
	                    	progEvent = progressEvent;
	                    	let curTime = new Date();
	                    	//seconds
	                    	let delTime = (curTime - startTime)/1000;
	                    	
	                    	let delBytes = progressEvent.loadedBytes;
	                    	let uploadSpeed = delBytes/delTime;
	                    	let remainedTime = (fileSize-delBytes)/uploadSpeed;
	                    	let msg = 'Downloading '+fileTitle+' ('+(delBytes/1024).toFixed(2)+ ' of '+ (fileSize/1024).toFixed(2)+" KB)";
	                    	if(remainedTime < lastRemained) {
	                    		lastRemained = remainedTime;
	                    	}
	      		    	document.getElementById('uploadStatusMessage').innerHTML = msg
	      		    		+'<br/>Estimated remained time (mm:ss) ' + Math.round(lastRemained/60)+':'+Math.round(lastRemained%60);
	      		  		progEvent = progressEvent;
	      		    	
	                  }
	                  
	                  
	                  
	              });
	               const data = await downloadResponse.blobBody;
	               
	               
	               //somehow Edge does not work with url from downloaded data
	               //window.URL.createObjectURL(await downloadResponse.blobBody);
	               //so we have to use the original url
	               const url = window.URL.createObjectURL(data);
	               const a = document.createElement('a');
	               a.style.display = 'none';
	               a.href = url;
	               a.download = fileTitle;
	               document.body.appendChild(a);
	               a.click();
	             	//No need to clean up after objectURL and it also cause EDGE to fail saving the downloaded
	               //window.URL.revokeObjectURL(url);
	           // }
	        
	    } catch (error) {
	        console.log(error);
	    }
	}
	proceedDownload();

}

function uploadf(fileInput) {
	const sasUrl = "https://imsstorage.blob.core.usgovcloudapi.net/testcontainer?sp=racwdli&st=2023-11-02T14:35:04Z&se=2023-11-02T22:35:04Z&spr=https&sv=2022-11-02&sr=c&sig=XZ5k3W72EOnMIJ5xt1s9WYeLWE%2FJZOwPgQA%2FupcTZpc%3D" ;
	const containerURL = new azblob.ContainerURL(sasUrl,
	    							azblob.StorageURL.newPipeline(new azblob.AnonymousCredential));
//upload blobs
const uploadFiles = async () => {
	//const fileInput = document.getElementById("file-input");
	try {
    	let nw = new Date();
    	let hr = (nw.getHours() < 10)? "0"+nw.getHours():nw.getHours();
    	let mi = (nw.getMinutes() < 10)? "0"+nw.getMinutes():nw.getMinutes();
    	let se = (nw.getSeconds() < 10)? "0"+nw.getSeconds():nw.getSeconds();
        
        const promises = [];
        for (const file of fileInput.files) {
        	reportStatus("Uploading files..."+file.name+" - "+hr+":"+mi+":"+se);
        	const fileSize = (file.size/1024/1000).toFixed(2);
            const blockBlobURL = azblob.BlockBlobURL.fromContainerURL(containerURL, file.name);
            promises.push(azblob.uploadBrowserDataToBlockBlob(
                azblob.Aborter.none, file, blockBlobURL,
                {
                    progress: (progressEvent) => {
                      //console.log((progressEvent.loadedBytes/1024/1000).toFixed(2)+'/'+fileSize);
                    	reportStatus((progressEvent.loadedBytes/1024/1000).toFixed(2)+'/'+fileSize);
                    }
                }));
        }
        await Promise.all(promises);
        nw = new Date();
        hr = (nw.getHours() < 10)? "0"+nw.getHours():nw.getHours();
    	mi = (nw.getMinutes() < 10)? "0"+nw.getMinutes():nw.getMinutes();
    	se = (nw.getSeconds() < 10)? "0"+nw.getSeconds():nw.getSeconds();
        reportStatus("Done. - "+hr+":"+mi+":"+se);
        listFiles();
    } catch (error) {
        reportStatus(error.body.message);
    }
}

uploadFiles();
//selectButton.addEventListener("click", () => fileInput.click());
//fileInput.addEventListener("change", uploadFiles);
}
function getAccessCode() {
	alert("here");
  		
var details = {
    'grant_type': 'client_credentials',
    'client_id': 'd3c9f180-1ded-4f2a-9fed-b0d431b2314c',
    'client_secret': 'Ip.2v9B8TU-NZ6LKarJ_S581~wZ407jQAK',
    'resource': 'https://imsstorage.blob.core.usgovcloudapi.net/'
};

	var formBody = [];
	for (var property in details) {
  	var encodedKey = encodeURIComponent(property);
  	var encodedValue = encodeURIComponent(details[property]);
  	formBody.push(encodedKey + "=" + encodedValue);
}
	formBody = formBody.join("&");  		
	fetch('https://login.microsoftonline.us/a3757a17-78a8-4dd9-9161-e20eace536cd/oauth2/token', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
		},
		body: formBody
	})
		.then(response => response.json())
		.then(response => console.log(response))
		.catch(err => console.error(err));  		
  		
}

function uploadFile() {
}

async function xmlhttpUpload() {
	const status = document.getElementById("status");
	const file = document.getElementById("filex");
	const btn = document.getElementById("upload-btn");
	const progress = document.getElementById("progress-bar");
	const aem_code = await getAccessToken();
	const aem_token = await getCSRFToken(aem_code);	
	//const access_code = await getAccessToken();
	const access_code = await getVendorAccessToken();
	
	btn.addEventListener('click', function() {
		const userVideo = file.files[0];
		const payload = new FormData();
		payload.append('user-video', userVideo, 'user-video.mp4');
		
		const req = new XMLHttpRequest();
		req.open('POST', 'http://dsd19dsdlyap02/api/assets/uploadtest/'+userVideo.name );
		req.setRequestHeader('Content-Type', userVideo.type);		
		req.setRequestHeader('Authorization', 'Bearer ' + access_code);
		req.setRequestHeader('CSRF-Token',aem_token);
		req.upload.addEventListener('progress', function(e) {
			const percentComplete = (e.loaded / e.total)*100;
			progress.setAttribute('value', percentComplete);
			progress.nextElementSibling.innerText = Math.round(percentComplete)+"%";
		})
		
		req.addEventListener('load', function() {
			console.log(req.status);
			console.log(req.response);
		})
		req.send(payload);
	})
}

async function readFile(input) {
	
  let file = input.files[0];

  let reader = new FileReader();
  reader.readAsBinaryString(file);
  reader.onerror = function() {
    console.log(reader.error);
  };

 const aem_code = await getAccessToken();
 const aem_token = await getCSRFToken(aem_code);	
 const access_code = await getAccessToken();

fetch(
  "http://dsd19dsdlyap02/api/assets/uploadtest/"+file.name,
  {
   method: "POST",
   body: file,
   headers: {
    "Content-type": file.type,
//    "Authorization" : "Basic "+btoa('admin:admin')
    "Authorization": "Bearer " + access_code,
    "CSRF-Token": aem_token
   }
  }
 )
.then(response => {
  if (response.ok) {
    return response.json();
  } else {
    throw new Error('File upload failed');
  }
})
.then(data => {
  console.log('Server response:', data);
})
.catch(error => {
  console.error('Error uploading file:', error);
});


}


async function getAccessToken() {

var details = {
    'grant_type': 'refresh_token',
    'client_id': 'p9na0lmrb50l26lvl7pns611jl-umujzwaj',
    'client_secret': '3omk6hv4rk3jia27bt9n2fhtdv',
    'redirect_uri': 'http://localhost:8080/servlets/index.html',
    'refresh_token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJwOW5hMGxtcmI1MGwyNmx2bDdwbnM2MTFqbC11bXVqendhaiIsImlzcyI6IkFkb2JlIEdyYW5pdGUiLCJzdWIiOiJhZG1pbiIsImV4cCI6MTczMDU2NjU4OCwiaWF0IjoxNjk5MDMwNTg4LCJzY29wZSI6InZlbmRvci14X193cml0ZS1kYW0sb2ZmbGluZV9hY2Nlc3MiLCJjdHkiOiJydCJ9.a-vXUqr9jE3_usMZ0uzXJBJPCu-Q2G1ZHAFE8K0QpTo'
};

var formBody = [];
for (var property in details) {
	  var encodedKey = encodeURIComponent(property);
	  var encodedValue = encodeURIComponent(details[property]);
  	  formBody.push(encodedKey + "=" + encodedValue);
}
formBody = formBody.join("&");  


const response = await fetch(
  "http://dsd19dsdlyap02/oauth/token",
  {
   method: "POST",
   headers: {
	"Content-Type" : "application/x-www-form-urlencoded"
   },
   body: formBody
  }
 )
const accesstoken = await response.json(); 	
return accesstoken.access_token;
}

async function getVendorAccessToken() {

	var details = {
		'code': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJwOW5hMGxtcmI1MGwyNmx2bDdwbnM2MTFqbC11bXVqendhaiIsInN1YiI6ImFkbWluIiwiZXhwIjoxNjk5MDMxMDM4LCJpYXQiOjE2OTkwMzA0MzgsInNjb3BlIjoidmVuZG9yLXhfX3dyaXRlLWRhbSxvZmZsaW5lX2FjY2VzcyIsImN0eSI6ImNvZGUifQ.TeVEc4IuxxOAyFRTlg7EesgI98EyyEhoX1xG60hVgzc',
	    'grant_type': 'authorization_code',
	    'redirect_uri': 'http://localhost:8080/servlets/index.html',
	    'client_id': 'p9na0lmrb50l26lvl7pns611jl-umujzwaj',
	    'client_secret': '3omk6hv4rk3jia27bt9n2fhtdv'
	};

	var formBody = [];
	for (var property in details) {
		  var encodedKey = encodeURIComponent(property);
		  var encodedValue = encodeURIComponent(details[property]);
	  	  formBody.push(encodedKey + "=" + encodedValue);
	}
	formBody = formBody.join("&");  


	const response = await fetch(
	  "http://dsd19dsdlyap02/oauth/token",
	  {
	   method: "POST",
	   headers: {
		"Content-Type" : "application/x-www-form-urlencoded"
	   },
	   body: formBody
	  }
	 )
	const accesstoken = await response.json(); 	
	return accesstoken.access_token;


	}

async function getCSRFToken(access_token) {
  var url = "http://dsd19dsdlyap02/libs/granite/csrf/token.json";	
  const token = await fetch(url, { 
	method: "GET", 
	headers: {
    "Authorization": "Bearer " + access_token
   	}
  })
  const aemtoken = await token.json();
  return aemtoken.token;
}

async function downloadFile() {
   const access_code = await getAccessToken();
 // const access_code = await getVendorAccessToken(); 
//  const aem_token = await getCSRFToken(access_code);	
//  var url = "http://dsd19dsdlyap02/api/assets/uploadtest/dsd.mp4";
  var url = "http://dsd19dsdlyap02/content/dam/uploadtest/smallfile55.mp4"; 
  
  const blob1 = await fetch(url, { 
	method: "GET", 
	headers: {
    "Authorization": "Bearer " + access_code
   }
    }
	)
    .then((res) => res.blob())
    .then(blob => {
    	console.log(blob)
    	document.querySelector('#myvideo').src = URL.createObjectURL(blob);
//    .then((res) => {
//      const aElement = document.createElement("a");
//      aElement.setAttribute("download", "cm.mp4");
//      const href = URL.createObjectURL(res);
//      aElement.href = href;
//      aElement.setAttribute("target", "_blank");
//      aElement.click();
//      URL.revokeObjectURL(href);
console.log("!!!!!");
    	
    });
    
}

async function getFromAjax() {
	const vurl = 'http://dsd19dsdlyap02/content/dam/uploadtest/midsize.mp4';
	const access_code = await getAccessToken();
	$('#playVideo').click(function (){

	    //console.log("json obje :"+ JSON.stringify(jsonObj))
	    // Rest call to play videos.
	    $.ajax({
	        type : 'GET',
	        url : vurl,
	        //dataType : 'json',
	        headers: { 'Authorization': 'Bearer '+ access_code  },
	        processData : false,
	        crossDomain : true,
	        success : function(result) {
	            //console.log("login result : " + JSON.stringify(result));
	            if (result) {
	                console.log("success.....");
	                srcPath = "data:video/mp4;"+result;
	                $('#videoTab').attr('src', vurl);
	                $('#videoTab').css('display', 'block');
	                $('#videoTab').attr('autoplay', true);
	            } else {
	                alert('failed...');
	            }
	        },
	        error : function(){
	            alert('error')
	        }
	    });
	});
	


}


