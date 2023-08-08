// redis
const redis = require('redis');
const client = redis.createClient({
    socket: {
        host: 'i9a203.p.ssafy.io',
        port: '6379'
    },
    username: '',
    password: ''
});
client.on('connect', () => {
    console.info('Redis connected!');
});
client.on('error', (err) => {
    console.log('Redis Server Error', err)
});
client.connect().then();

// express
const express = require('express');
const app = express();

// constrants
const PORT = process.env.PORT || 3010;

app.get('/:iot_command', async (req, res) => {
    console.log('GET /' + req.params.iot_command);
    if(await client.exists('iot' + req.params.iot_command)) {
        res_value = await client.get('iot' + req.params.iot_command);
        res.send(res_value);
    }
    else {
        res.send('0');
    }
});

app.get('*', async (req, res) => {
    console.log('DEFAULT GET: ' + req.originalUrl);
    res.send('8');
});

/*
app.get('/default', async (req, res) => {
    console.log('DEFAULT /' + req.params.iot_command);
    res_value = await client.get('iot1_toy');
    console.log('initinal value: ' + res_value);
    res_value = await client.set('iot1_toy', '5');
    res_value = await client.get('iot1_toy');
    console.log('changed value: ' + res_value);
    res.send(res_value);
    console.log('END');
});
*/

app.listen(PORT, () => console.log('Listening on port', PORT));