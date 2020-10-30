//function uploadFile() {
//    var blobFile = $('#filechooser').files[0];
//    var formData = new FormData();
//    formData.append("fileToUpload", blobFile);
//
//    $.ajax({
//       url: "upload.php",
//       type: "POST",
//       data: formData,
//       processData: false,
//       contentType: false,
//       success: function(response) {
//           alert("success");
//       },
//       error: function(jqXHR, textStatus, errorMessage) {
//           console.log(errorMessage);
//       }
//    });
//}
//
//function send() {
//    if (this.val != '') alert('Выбрано файлов: ' + this.files.length);
//    else alert('Выберите файлы');
//};
//
//document.getElementById('image_button').onclick = send;