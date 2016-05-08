// Следующий блок обеспечивает наличие свойства outerHTML у всех компонент
if (typeof(HTMLElement) != "undefined") 
{
    var _emptyTags = {
       "IMG": true,
       "BR": true,
       "INPUT": true,
       "META": true,
       "LINK": true,
       "PARAM": true,
       "HR": true
    };
    
    HTMLElement.prototype.__defineGetter__("outerHTML", function () {
       var attrs = this.attributes;
       var str = "<" + this.tagName;
       for (var i = 0; i < attrs.length; i++)
          str += " " + attrs[ i ].name + "=\"" + attrs[ i ].value + "\"";
    
       if (_emptyTags[this.tagName])
          return str + ">";
    
       return str + ">" + this.innerHTML + "</" + this.tagName + ">";
    });
    
    HTMLElement.prototype.__defineSetter__("outerHTML", function (sHTML) {
       var r = this.ownerDocument.createRange();
       r.setStartBefore(this);
       var df = r.createContextualFragment(sHTML);
       this.parentNode.replaceChild(df, this);
    });
}
//==============================================
window._common = 
{
  getFields: function(obj) {
                       var rc = "";
                       for (var att in obj) {
                              rc += (att + ",");
                       }
                       return rc;
            },
   imageResize:  function(maxWidth,maxHeight,img) {
                     if (img.width > img.height) { 
                         ratio = maxWidth / img.width;
                     }
                     else {    
                       ratio = maxHeight / img.height;
                     }
                             
                     newWidth  = ratio * img.width;
                     newHeight = ratio * img.height;
                          
                     img.width  = newWidth; 
                     img.height = newHeight; 
                },

   isEmpty : function (str) {
               if (!str || str.length == 0) {
                  return true;
               }
               return false;
            },

   ltrim : function(x) {
      x = x.replace(/^\s*(.*)/, "$1");
      return x;
   },

  rtrim : function (x) {
      x = x.replace(/(.*?)\s*$/, "$1");
      return x;
   },

   trim : function (x) {
      x = _common.ltrim(x);
      x = _common.rtrim(x);
      return x;
   },
   
   getScreenResolutionWidth : function() 
   {
     return window.screen.width;
   },
   
   getScreenResolutionHeight : function() 
   {
     return window.screen.height;
   },
   
   getClientWidth : function()
   {  
      return document.compatMode=='CSS1Compat' && 
            !window.opera?document.documentElement.clientWidth:document.body.clientWidth;
   },
   
   getClientHeight : function()
   {  
      return document.compatMode=='CSS1Compat' && 
           !window.opera?document.documentElement.clientHeight:document.body.clientHeight;
   },
   
   parseInt : function (val) {
	if (!val) {
		return 0;
	}
	val = val.replace("px", "");
	val = val.replace("pt", "");
	return Number(val);
   },
   
     centerWindowLocation : function (wWin,hWin) 
	{
	  var wClient = window.screen.width;
	  var hClient = window.screen.height;
	  var rc = {x:0, y:0};
	  rc.x = (wClient - wWin)/2;
	  rc.y = (hClient - hWin)/2;
	  rc.x = Math.round(rc.x);
	  rc.y = Math.round(rc.y);
	  if (rc.x < 0) 
	  {
	    rc.x = 0;
	  }
	  if (rc.y < 0) 
	  {
	    rc.y = 0;
	  }
	  return rc;
	},
     
     destroyRightCardInfo : function() 
     {
       var basePath = window.basePath;
       var path = basePath+"/servletForRightsManagementBean?method=destroyRightCardTemplate&rand="+Math.random();
       jQuery.post(path);
     },
     
     queryString : function(parameter) {
       var loc = location.search.substring(1, location.search.length);
       var param_value = false;
       var params = loc.split("&");
       for (i=0; i < params.length; i++) {
         param_name = params[i].substring(0,params[i].indexOf('='));
         if (param_name == parameter) {
             param_value = params[i].substring(params[i].indexOf('=')+1)
         }
       }
       if (param_value) {
           return param_value;
       }
       else {
           return false; //Here determine return if no parameter is found
       }
     } //queryString
};
//================== Обычные функции =============================
function showPP(event) 
{
   event.cancel();
   var src = event.getSource();
   //----------------------------------
   var index = src.getProperty("index");
   var url0 = src.getProperty("url0");
   var unom = src.getProperty("unom");
   var ou_id = src.getProperty("ou_id");
   var w = 800;
   var h = 768;
   var xy = window._common.centerWindowLocation(w,h);
   
   /*
   var sFeatures = 
    "dialogLeft:"+xy.x+";dialogTop:"+xy.y+
    ";dialogWidth:"+w+"px;dialogHeight:"+h+"px;"+
    "resizable:1;edge:raised;status:0;unadorned:0";
   //window.status = "Построение поэтажных планов ..."; 
   //window.status = "\u041f\u043e\u0441\u0442\u0440\u043e\u0435\u043d\u0438\u0435 \u043f\u043e\u044d\u0442\u0430\u0436\u043d\u044b\u0445 \u043f\u043b\u0430\u043d\u043e\u0432 ..."; 
   window.showModalDialog(url0+"?index_table="+index+"&unom="+unom+"&ou_id="+ou_id ,window ,sFeatures);
   //window.status = ""; 
   */
   var sFeatures = "left="+xy.x+",top="+xy.y+",width="+w+",height="+h+","+"resizable=1,status=0";
   window.open(url0+"?index_table="+index+"&unom="+unom+"&ou_id="+ou_id,
               null,
               sFeatures
   );

}
function notImpl(event) 
{
   event.cancel();
   //window.alert("Пока не реализовано");  
   window.alert("\u041f\u043e\u043a\u0430 \u043d\u0435 \u0440\u0435\u0430\u043b\u0438\u0437\u043e\u0432\u0430\u043d\u043e");  
}
function winClose(event) {
  event.cancel();
  window.close();
}
function winPrintSimple(event) {
  event.cancel();
  window.print();
}
function printImage(event) {
  event.cancel();
  var src = event.getSource();
  var imageId = src.getProperty("imageId");
  var img = document.getElementById(imageId);
  var win = window.open();
  jQuery(win.document.body).html(img.outerHTML);
}


