var admin = require("firebase-admin");

// add your service token to run this service
var serviceAccount = require("./config/serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://android-covid-19-812f3.firebaseio.com"
});


const createPayload = (title, body) => {
  return {
    notification: {
      title: title,
      body: body
    }
  };
};

const createOptions = (priority, timeToLive) => {
  return {
    priority: priority,
    timeToLive: timeToLive
  };
}

const sendFirebaseMessage = (registrationToken, payload, options) => {
  return admin.messaging().sendToDevice(registrationToken, payload, options)
}


const handleSendMessage = (db) => (req, res) => {
  const { userid, message } = req.params;
  let receipientToken = db[userid];
  

  let payload = createPayload("This is a Notification", message);
  let options = createOptions("high", 60 * 60 *24);

  sendFirebaseMessage(receipientToken, payload, options)
    .then(function (response) {
      res.status(200).json(response);
      console.log("Successfully sent message:", response);
    })
    .catch(function (error) {
      res.status(404).json('Unable to send push notification');
      console.log("Error sending message:", error);
    });
}

module.exports = {
  handleSendMessage
}