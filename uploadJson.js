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



var menu = require("./서울특별시 문화행사 정보.json");
menu.DATA.forEach(function(obj) {
  var config = {
    title: obj.title,
    publisher: "administrator",
    category: 102,
    description: ""
  };
  if (!isNull(obj.place)) {
    config.venue = obj.place;
  }
  if (!isNull(obj.main_img)) {
    config.poster = obj.main_img;
  }
  if (!isNull(obj.period)) {
    config.period = obj.period;
  }
  if (!isNull(obj.codename)) {
    config.subjectCategory = obj.codename;
  }
  if (!isNull(obj.org_name)) {
    config.creator = obj.org_name;
  }
  if (!isNull(obj.date)) {
    config.date = obj.date;
  }
  if (!isNull(obj.org_link)) {
    config.url = obj.org_link;
  }
  if (!isNull(obj.end_date)) {
    config.end_date = new Date(obj.end_date);
  }
  if (!isNull(obj.use_trgt)) {
    config.description = config.description + "\n"
    obj.use_trgt;
  }
  if (!isNull(obj.use_fee)) {
    config.description = config.description + "\n" + obj.use_fee;
  }
  if (!isNull(obj.strtdate)) {
    config.start_date = new Date(obj.strtdate);
  }
  if (!isNull(obj.program)) {
    config.description = obj.program;
  }
  db.collection("All").doc(obj.title).set(config, {
      merge: true
    }).then(function(docRef) {
      console.log("Document written with ID: ");
    })
    .catch(function(error) {
      console.error("Error adding document: ", error);
    });
});
