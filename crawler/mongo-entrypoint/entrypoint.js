var db = connect('mongodb://admin:pass@localhost:27017');

db = db.getSiblingDB('crawler'); // we can not use "use" statement here to switch db

db.createUser({
  user: 'user',
  pwd: 'pass',
  roles: [{ role: 'readWrite', db: 'crawler' }],
  passwordDigestor: 'server',
});
