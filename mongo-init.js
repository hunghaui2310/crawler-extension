db = db.getSiblingDB('crawler');
db.createUser({
  user: 'user',
  pwd: 'pass',
  roles: [{ role: 'readWrite', db: 'crawler' }],
  passwordDigestor: 'server',
});

db.createCollection('product');
db.createCollection('product_raw_data');
db.createCollection('shop');
db.createCollection('shop_product_raw_data');
db.createCollection('shop_raw_data');