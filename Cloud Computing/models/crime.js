const config = require('../config/databaseConfig')
const Sequelize = require('sequelize');

const model = {
  'id': {
    autoIncrement: true,
    primaryKey: true,
    type: Sequelize.INTEGER
  },
  'namaBis': Sequelize.STRING,
  'namaSupir': Sequelize.STRING,
  'tujuanBis': Sequelize.STRING,
  'image': Sequelize.STRING,
  'tanggalkejadian': Sequelize.STRING,
  'lat': Sequelize.DOUBLE,
  'lon': Sequelize.DOUBLE
};
module.exports = config.define('tb_crimes', model, {
  freezeTableName: true,
  timestamps: false,
  timezone: 'Asia/Jakarta',
})
