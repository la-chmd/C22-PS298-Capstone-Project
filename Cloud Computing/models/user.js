const config = require('../config/databaseConfig')
const Sequelize = require('sequelize');

const model = {
  'id': {
    autoIncrement: true,
    primaryKey: true,
    type: Sequelize.INTEGER
  },
  'username': Sequelize.STRING,
  'password': Sequelize.STRING
};
module.exports = config.define('tb_users', model, {
  freezeTableName: true,
  timestamps: false,
  timezone: 'Asia/Jakarta',
})
