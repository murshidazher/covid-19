const express = require('express');
const bodyParser = require('body-parser');

const send = require('./send');

const PORT = process.env.PORT || 4001;

const app = express();

app.use(bodyParser.json());

var tokenDatabase = {1: "cILcJK0-YFs:APA91bGLXHF6zhbJDa0YyBkuTCiqVenZxDnNh_tryg7dwgQxyV2ACEDdENLAkApri_xiaNe6YSfKcIswMy7Gq-T6L6BG_uUuMKyivwtZZ_pI3KB7iZ_QRTOZ4vUdwnSSKT_cRs4NpxZN"};

app.get('/', (req, res) => {
  res.json({ 'greeting': 'Helloo' });
})

app.post('/register/:userid/:fcmToken', (req,res) => {
    const { userid, fcmToken } = req.params;
    tokenDatabase[userid] = fcmToken;
    
    res.json({ userid: fcmToken + ' ' +  userid});
});

app.get('/send/:userid/:message', (req,res) => send.handleSendMessage(tokenDatabase));


app.listen(PORT, () => {
	console.log(`🚀 Server is listening on port ${PORT}`)
})


// // Serve the static files from the React app
// app.use(express.static(path.join(__dirname, 'client/build')));

// // An api endpoint that returns a short list of items
// app.get('/api/getList', (req,res) => {
//     var list = ["item1", "item2", "item3"];
//     res.json(list);
//     console.log('Sent list of items');
// });

// // Handles any requests that don't match the ones above
// app.get('*', (req,res) =>{
//     res.sendFile(path.join(__dirname+'/client/build/index.html'));
// });

// const port = process.env.PORT || 5000;
// app.listen(port);

// console.log('App is listening on port ' + port);