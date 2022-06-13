const crimeModel = require("../models/crime");
const response = require("../utils/response");
const moment = require("moment");
const date = new Date();

exports.inputCrime = data =>
  new Promise((resolve, reject) => {
    crimeModel.findAll({})
      .then(dataModel => {
        console.log(dataModel.length + 1)
        Object.assign(data, {
          id: dataModel.length + 1,
          tanggalkejadian: moment(Date.now()).format('MM/DD/YYYY')
        })
        crimeModel
          .create(data)
          .then(() => resolve(response.commonSuccess))
          .catch(err => {
            console.log(err);
            reject(response.commonError);
          });
      })
  });

exports.getCrimebyTanggal = tanggal =>
  new Promise(async (resolve, reject) => {
    crimeModel
      .findAll({
        where: {
          tanggalkejadian: tanggal
        }
      })
      .then(result => {
        resolve(response.commonResult(result));
      })
      .catch(err => {
        console.error(err);
        reject(response.commonError);
      });
  });

exports.getCrime = () =>
  new Promise(async (resolve, reject) => {
    crimeModel
      .findAll({})
      .then(result => {
        resolve(response.commonResult(result));
      })
      .catch(err => {
        console.error(err);
        reject(response.commonError);
      });
  });