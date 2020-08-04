const functions = require('firebase-functions');
var bodyParser = require('body-parser');
var express = require('express');
var http = require('http');
var firebase = require("firebase");
var firebaseAdmin = require("firebase-admin");
var firebaseFunctions = require("firebase-functions");
var app = express();

var firebaseConfig = {
  apiKey: "AIzaSyCFIPnKDcMHL2VdpGmUleT0qz1KJYsl6K0",
  authDomain: "my-project-73029-baca5.firebaseapp.com",
  databaseURL: "https://my-project-73029-baca5.firebaseio.com",
  projectId: "my-project-73029-baca5",
  storageBucket: "my-project-73029-baca5.appspot.com",
  messagingSenderId: "905164925602",
  appId: "1:905164925602:web:0c675bc7fc68cc171505fb",
  measurementId: "G-7TQFXVHNXZ"
};
firebase.initializeApp(firebaseConfig);
firebase.analytics();

app.use(bodyParser.urlencoded({
  extended: false
}));

app.post("/", function(req, res) {
  var radio = document.querySelector('input[name="category"]:checked').value;
  var cate;
  if (radio == '전시') {
    cate = 101;
  } else {
    cate = 102;
  }

  var addData = {
    creator: req.body.creator,
    title: req.body.title,
    venue: req.body.venue,
    startPeriod: firebase.firestore.Timestamp.fromDate(new Date(req.body.startPeriod)),
    endPeriod: firebase.firestore.Timestamp.fromDate(new Date(req.body.endPeriod)),
    publisher: req.body.publisher,
    poster: req.body.poster,
    description: req.body.description,
    category: cate
  };
  var db = firebase.firestore().collection('Show').doc(title);

  firebase.auth().onAuthStateChanged(function(user) {
    var userDB = firebase.firestore().collection('BusinessUser').doc(user.email);
    userDB.get().then(function(doc) {
      var data = doc.data();
      if (data != null) {
        if (data.permission == null) {
          alert("데이터 추가 권한 받기");
          res.render('my_page.html');
        } else {
          if (data.permission == true) {
            db.set(addData).then(function() {
              console.log("데이터 추가 완료");
              alert("데이터 추가 성공");
              res.redirect("List.html");
            }).catch(function(error) {
              console.log(errorMessage);
              alert("데이터 추가 실패");
              res.redirect("List.html");
            });
          } else {
            alert("데이터 추가 권한 받기");
            res.render('my_page.html');
          }
        }
      } else {
        alert("데이터 추가 권한 받기");
        res.render('my_page.html');
      }
    });
  });
});
exports.app = functions.https.onRequest(app);
// http.createServer(app).listen(5000,function(){
//   console.log("server start");
// });
