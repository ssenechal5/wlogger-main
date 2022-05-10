// imports
const Koa = require('koa');
const serve = require('koa-static');
const mount = require('koa-mount');
const proxy = require('koa-server-http-proxy');
const addTrailingSlashes = require('koa-add-trailing-slashes');
const PropertiesReader = require('properties-reader');

// configuration properties
const properties = PropertiesReader(__dirname + '/../src/main/resources/application.properties');
const serverPort = getProperty(properties, 'quarkus.http.port');
const thingparkUrl = getProperty(properties, 'wlogger.smp-client.uri');
const keycloakUrl = properties.get('%dev.keycloak.uri') || thingparkUrl;

// http server
const app = new Koa();
app.use(addTrailingSlashes());
// app.use(mount('/thingpark/wlogger/gui', serve(__dirname + '/../wlogger-webapp/sources/webapp/_buildFolder')));
app.use(mount('/thingpark/wlogger/gui', serve(__dirname + '/../wlogger-webapp/sources/webapp/sources')));
app.use(proxy('/thingpark/wlogger/rest', {
    target: `http://localhost:${serverPort}`,
    changeOrigin: true
}));
app.use(proxy('/thingpark/smp', {
    target: thingparkUrl,
    changeOrigin: true,
    secure: false,
    protocolRewrite: 'https',
}));
app.use(proxy('/auth', {
    target: keycloakUrl,
    changeOrigin: true,
    secure: false,
    protocolRewrite: 'https',
}));
app.use(proxy('/auth-realm', {
    target: keycloakUrl,
    changeOrigin: true,
    secure: false,
    protocolRewrite: 'https',
}));
app.listen(8281);

function getProperty(properties, name) {
    return properties.get(`%dev.${name}`) || properties.get(name);
}