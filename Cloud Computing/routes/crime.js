const router = require('express').Router()
const crimeController = require('../controllers/crime')
const uploadSetting = require('../utils/imageConfig')
const path = require('path')
const fs = require('fs')
// const { route } = require('../profileBaja')
const fields = uploadSetting.upload.fields([
  {
    name: 'image',
    maxCount: 100
  }
])

router.post('/inputCrime', fields, (req, res) => {
  const imageName = uploadSetting.cekNull(req.files['image'])
  const data = Object.assign(req.body, {
    image: imageName,
  })
  // console.log(data)
  crimeController.inputCrime(data)
    .then(result => res.json(result))
    .catch(err => res.json(err))
})

router.get('/getCrime', (req, res) => {
  crimeController.getCrime()
    .then(result => res.json(result))
    .catch(err => res.json(err))
})

router.get('/getCrime/:tanggal', (req, res) => {
  crimeController.getCrimebyTanggal(req.params.tanggal)
    .then(result => res.json(result))
    .catch(err => res.json(err))
})

router.post('/editImage/:id', fields, (req, res) => {
  // console.log(req.files['image'])
  const imageName = uploadSetting.cekNull(req.files['image'])
  const netoImage = uploadSetting.cekNull(req.files['netoImage'])
  var profile = req.body.nameProfiles;
  console.log(profile)
  if (profile == undefined || profile == null) {
    profile = "null"
  } else {
    profile = profile
  }

  console.log(profile)
  // console.log (req.files['image'])
  // console.log(req.body)
  const data = JSON.parse(req.body.data)
  let changeNeto = false
  if (netoImage) {
    changeNeto = true
    data = Object.assign(data, {
      netoImage: netoImage,
      oldNeto: data.netoImage
    })
  }

  ImageSikuController.updateImage(data, req.params.id, changeNeto)
    .then(result => res.json(result))
    .catch(err => res.json(err))
})

router.delete('/deleteAll', (req, res) => {
  ImageSikuController.deleteAll()
    .then(result => res.json(result))
    .catch(err => res.json(err))
})

router.delete('/deleteimage/:id', (req, res) => {
  // console.log(req.params.id)
  ImageSikuController.deleteImage(req.params.id)
    .then(result => res.json(result))
    .catch(err => res.json(err))
})

router.get('/imageFull', (req, res) => {
  var image = []
  const directoryPath = path.join(__dirname, '../../public/images/');
  //passsing directoryPath and callback function
  fs.readdir(directoryPath, function (err, files) {
    //handling error
    if (err) {
      return console.log('Unable to scan directory: ' + err);
    }
    //listing all files using forEach
    files.forEach(function (file) {
      // Do whatever you want to do with the file
      // console.log(file); 
      image.push(file)
    });
    res.json(image)
  });
})

router.delete('/Imagedel/:image', (req, res) => {
  const imageName = req.params.image

  console.log(imageName)
  fs.unlinkSync(`./public/images/${imageName}`)
  res.json('Berhasil Menghapus')

})

router.delete('/alldell', (req, res) => {
  const directoryPath = path.join(__dirname, '../public/image/');
  //passsing directoryPath and callback function
  fs.readdir(directoryPath, function (err, files) {
    //handling error
    if (err) {
      return console.log('Unable to scan directory: ' + err);
    }
    //listing all files using forEach
    files.forEach(function (file) {
      // Do whatever you want to do with the file
      // console.log(file); 
      if (file !== 'index.html')
        fs.unlinkSync(`./public/image/${file}`)
    });
    res.json('Yeahh')
  });

})

module.exports = router