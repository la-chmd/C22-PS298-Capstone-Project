const userModel = require("../models/user");
const response = require("../utils/response");
const bcrypt = require("bcrypt")

exports.register = data =>
  new Promise((resolve, reject) => {
    userModel.findAll({
      where: {
        username: data.username
      }
    }).then((user) => {
      if (user.length > 0) {
        reject(response.commonErrorMsg('Username Telah Terdaftar'))
      } else {
        bcrypt.hash(data.password, 10, (err, hash) => {
          data.password = hash
          userModel
            .create(data)
            .then(() => resolve(response.commonSuccess))
            .catch(err => {
              console.log(err);
              reject(response.commonError);
            });
        })
      }
    })
  });

exports.login = data =>
  new Promise((resolve, reject) => {
    userModel
      .findOne({
        where: {
          username: data.username
        }
      })
      .then((user) => {
        if (user) {
          if (bcrypt.compareSync(data.password, user.password)) {
            resolve(response.commonResult(user))
          } else {
            reject(response.commonErrorMsg('Password Salah'))
          }
        } else {
          reject(response.commonErrorMsg('Username Tidak Terdaftar'))
        }
      })
      .catch(err => {
        console.error(err);
        reject(response.commonError);
      });
  });