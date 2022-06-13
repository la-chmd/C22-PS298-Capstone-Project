const multer = require('multer')
const path = require('path')
const MAX_SIZE = 90000000000
const fs = require('fs')

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, path.join(__dirname, '../public/image/'))
  },
  filename: (req, file, cb) => {
    cb(null, new Date().toISOString().replace(/:/g, '-') + path.extname(file.originalname))
  }
})

const fileFilter = (req, file, cb) => {
  if (file.mimetype === 'image/jpeg' || timeLog.mimetype === 'image/.png') {
    cb(null, true)
  } else {
    cb(null, false)
  }
}

const upload = multer({
  storage: storage,
  limits: {
    fileSize: MAX_SIZE
  }
})

const cekNull = (fileUpload) => {
  // console.log(__dirname, '../statics')
  // console.log(fileUpload)
  if (fileUpload.length === 0 || fileUpload.length === null) {
    return null
  } else {
    return fileUpload[0].filename
  }
}

const deleteImage = (data) => {
  // console.log(data)
  var image = data.image
  var Segaris = data.Segaris
  var takSegaris = data.takSegaris
  console.log(takSegaris)
  var i = 0;
  for (i; i < image.length; i++) {
    fs.unlinkSync(`./public/images/${image[i]}`)
    fs.unlinkSync(`./public/images/${Segaris[i]}`)
    if (takSegaris !== null) {
      fs.unlinkSync(`./public/images/${takSegaris[i]}`)
    }
    // console.log(image[i])
  }
  // fs.unlinkSync(`../statics/${image}`)
}

module.exports = { multer, upload, cekNull, deleteImage }