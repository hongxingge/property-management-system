/**
 * file转base64
 */
const fileToBase64 = (blob, cb) => {
    let reader = new FileReader();
    reader.onload = function (evt) {
        let base64 = evt.target.result;
        cb(base64);
    };
    reader.readAsDataURL(blob);
};
export default fileToBase64;
