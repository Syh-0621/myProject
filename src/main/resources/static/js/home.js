let pids = document.querySelectorAll("#pid");
document.addEventListener("DOMContentLoaded", function() {
    pids.forEach(function (pidEle) {
        pid = pidEle.innerText;
        axios.post("/getproduct?pid="+pid, {
            "pid": pid
        }).then(function ({data}) {
            let images = data.image;
            images = images.split(",");
            let imageContainer = document.createElement("div");
            imageContainer.style.display = "flex";
            imageContainer.style.flexWrap = "wrap";
            images.forEach(image => {
                const img = document.createElement("img");
                img.src = image;
                img.width = 200;
                img.height = 200;
                imageContainer.appendChild(img);
            })
            pidEle.after(imageContainer);
        })
    })
})