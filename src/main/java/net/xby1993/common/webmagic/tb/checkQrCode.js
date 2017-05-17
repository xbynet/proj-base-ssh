function getStyle(obj, attr) {
    if (obj.currentStyle) {
        return obj.currentStyle[attr];
    } else {
        return document.defaultView.getComputedStyle(obj, null)[attr];
    }
}
var tmp=document.querySelector(".qrcode-main> .msg-err");
var display=getStyle(tmp,"display");

return display;