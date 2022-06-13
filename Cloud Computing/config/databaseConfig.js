const Sequelize = require('sequelize');

const DB_USER = "postgres"
const DB_PASSWORD = "@R1kikun"
const DB_HOST = "104.197.118.216"
const DB_PORT = 5432
const DB_DATABASE = "dbcrime"
require('dotenv').config()
const sequelize = new Sequelize(DB_DATABASE, DB_USER, DB_PASSWORD, {
  host: DB_HOST,
  dialect: 'postgres',
  pool: {
    max: 5,
    min: 0,
    idle: 10000
  }
});
try {
  sequelize.authenticate(() => {
    console.log("Konek nih")
  });
} catch (error) {
  console.error('Unable to connect to the database:', error);
}
module.exports = sequelize



