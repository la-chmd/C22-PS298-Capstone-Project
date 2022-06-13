const express = require("express");
const cors = require("cors");
const morgan = require("morgan");
const app = express();
const path = require("path");
const moment = require("moment");


app.use(cors());
app.use(morgan("dev"));

app.use(express.json());


app.use(express.urlencoded({ extended: true }));

app.get("/", (req, res) => {
  res.json({ message: "Welcome to my api" });
});

app.use("/crime", require("./routes/crime"));
app.use("/user", require("./routes/user"));


app.use(
  "/image",
  express.static(path.join(__dirname, "public/image"))
);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}.`);
});
