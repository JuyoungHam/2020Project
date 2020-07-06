var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;

const firebase = require("firebase");
// Required for side-effects
require("firebase/firestore");

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

// var jsonFile = menu =[
//     {
//        "id":1,
//        "name":"Focaccia al rosmarino",
//        "description":"Wood fired rosemary and garlic focaccia",
//        "price":8.50,
//        "type":"Appetizers"
//     },
//     {
//        "id":2,
//        "name":"Burratta con speck",
//        "description":"Burratta cheese, imported smoked prok belly prosciutto, pached balsamic pear",
//        "price":13.50,
//        "type":"Appetizers"
//     }
//  ];
var json = require('1.json');

data.forEach(function(obj) {
  db.collection("temp").document(obj.title).add({
      title: obj.title,
      venue: obj.venue,
      period: obj.period,
      url: obj.url,
      category: 101,
      poster: obj.referenceIdentifier
    }).then(function(docRef) {
      console.log("Document written with ID: ", docRef.id);
    })
    .catch(function(error) {
      console.error("Error adding document: ", error);
    });
});
