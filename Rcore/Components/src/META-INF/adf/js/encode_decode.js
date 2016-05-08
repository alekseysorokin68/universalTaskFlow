RUS_ALL_LETTERS = 
"\u0419\u0426\u0423\u041a\u0415\u041d\u0413\u0428\u0429\u0417\u0425\u042a\u0424\u042b\u0412\u0410\u041f\u0420\u041e\u041b\u0414\u0416\u042d\u042f\u0427\u0421\u041c\u0418\u0422\u042c\u0411\u042e\u0401\u0439\u0446\u0443\u043a\u0435\u043d\u0433\u0448\u0449\u0437\u0445\u044a\u0444\u044b\u0432\u0430\u043f\u0440\u043e\u043b\u0434\u0436\u044d\u044f\u0447\u0441\u043c\u0438\u0442\u044c\u0431\u044e\u0451"+
'\x00'+
'\x01'+
'\x02'+
'\x03'+
'\x04'+
'\x05'+
'\x06'+
'\x07'+
'\x08'+
'\x09'+
'\x0A'+
'\x0B'+
'\x0C'+
'\x0D'+
'\x0E'+
'\x0F'+
'\x10'+
'\x11'+
'\x12'+
'\x13'+
'\x14'+
'\x15'+
'\x16'+
'\x17'+
'\x18'+
'\x19'+
'\x1A'+
'\x1B'+
'\x1C'+
'\x1D'+
'\x1E'+
'\x1F';
PREFIX_ENCODE = "~`";
SUFFIX_ENCODE = "`~";

function decodeRusString(txt) {
  for(var i=0; i < RUS_ALL_LETTERS.length; i++) {
    var find = PREFIX_ENCODE + i +SUFFIX_ENCODE;
    var target = RUS_ALL_LETTERS.substring(i,i+1);
    var regexp = new RegExp(find,"g");
    txt = txt.replace(regexp,target); 
  }
  return txt;   
}

function encodeRusString(txt) 
{
  function encodeOneRusChar(index) {
      return PREFIX_ENCODE + index + SUFFIX_ENCODE;
  };
    if (!txt) {
      return txt;
    }
    var allRusChars = RUS_ALL_LETTERS;
    var rc = "";
    for (var i = 0; i < txt.length; i++) {
      var ch = txt.substring(i,i+1);
      var indexOf = allRusChars.indexOf(ch);
      if (indexOf == -1) {
        rc+=ch;
      }
      else {
        rc+=encodeOneRusChar(indexOf);
      }
    }
    return rc;
}