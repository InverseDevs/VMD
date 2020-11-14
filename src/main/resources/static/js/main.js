//$('.tabs').on('click', 'li a', function(e){
//    e.preventDefault();
//    var $tab = $(this),
//    href = $tab.attr('href');
//    $('.active').removeClass('active');
//    $tab.addClass('active');
//    $('.show')
//        .removeClass('show')
//        .addClass('hide')
//        .hide();
//
//    $(href)
//        .removeClass('hide')
//        .addClass('show')
//        .hide()
//        .fadeIn(550);
//});



function getBase64Image(){
    var p;
    var canvas = document.createElement("canvas");
    var img1=document.createElement("img");

    p=document.getElementById("uploadInput").value;
    img1.setAttribute('src', p);
    canvas.width = img1.width;
    canvas.height = img1.height;
    var ctx = canvas.getContext("2d");
    ctx.drawImage(img1, 0, 0);
    var dataURL = canvas.toDataURL("image/png");
    return dataURL;
}

function httpGet(theUrl) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "POST", theUrl, false );

    var obj = new Object();
    obj.avatar = getBase64Image();
    var jsonString= JSON.stringify(obj);

    xmlHttp.send(jsonString);
    return xmlHttp.responseText;
}

window.onload = function() {
  httpGet("localhost:8080/avatar");
};