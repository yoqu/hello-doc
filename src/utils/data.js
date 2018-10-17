export function dataToField(value) {
    if (value == null || value == undefined)
        return "";
    else
        return value;
}

export function dataToArray(value) {
    if (value == null || value == undefined)
        return [];
    else
        return value;
}
export function checkUrl(urlString){
  var reg=/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
  if(!reg.test(urlString)){
    return false
  }
  return true;
}
