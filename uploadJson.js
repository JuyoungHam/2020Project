var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;

const firebase = require("firebase");
// Required for side-effects
require("firebase/firestore");

function isNull(v) {
    return (v === undefined || v === null) ? true : false;
}

// Initialize Cloud Firestore through Firebase
firebase.initializeApp({
  apiKey: "AIzaSyCFIPnKDcMHL2VdpGmUleT0qz1KJYsl6K0",
  authDomain: "my-project-73029-baca5.firebaseapp.com",
  databaseURL: "https://my-project-73029-baca5.firebaseio.com",
  projectId: "my-project-73029-baca5",
  storageBucket: "my-project-73029-baca5.appspot.com",
  messagingSenderId: "905164925602",
  appId: "1:905164925602:web:86e064be612a13611505fb",
  measurementId: "G-S03TVY9QLX"
});

var db = firebase.firestore();


  
  var menu = require("./JsonFiles/"+7+".json");
  menu.response.msgBody.perforList.forEach(function(obj) {
    var config = {
      title: obj.title,
      poster: obj.thumbnail,
      category: 101,
	
    };
    if (!isNull(obj.place)) {
      config.venue = obj.place;
    }
    if (!isNull(obj.referenceIdentifier)) {
      config.poster = obj.referenceIdentifier;
    }
    if (!isNull(obj.period)) {
      config.period = obj.period;
    }
    if (!isNull(obj.realmName)) {
      config.subjectCategory = obj.realmName;
    }
    if (!isNull(obj.creator)) {
      config.creator = obj.creator;
    }
    if (!isNull(obj.url)) {
      config.url = obj.url;
    }


    db.collection("All").doc(obj.title).set(config).then(function(docRef) {
        console.log("Document written with ID: ");
      })
      .catch(function(error) {
        console.error("Error adding document: ", error);
      });
  });

