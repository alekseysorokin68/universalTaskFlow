//*** Tree Begin
selectUpSingleNode = function (node, tagName) {
  var rc = null;
  var parent = node;
  while (true) {
    if (!parent) {
      break;
    }
    if (parent.nodeName == tagName) {
      rc = parent;
      break;
    }
    parent = parent.parentNode;
  }
  return rc;
};

function selectUpSingleNodeByAttribute(node,tagName,att,value) {
  var rc = node;
  while(true) {
    if (!rc) {return null;}
    rc = selectUpSingleNode(rc,tagName);
    if (!rc) {return null;}
    //if (rc.getAttribute(attr) == value) {
    if (jQuery(rc).attr(att) == value) {
      return rc;
    }
    rc = rc.parentNode;
  } // while
}

function getFirstChildNode(node, tagName) {
  var childs = null;
  try {
    childs = node.childNodes;
  }
  catch (ex) {
    ;
  }
  try {
    if (!childs) {
      childs = node.children;
    }
  }
  catch (ex) {
    ;
  }
  try {
    if (!childs) {
      childs = [];
    }
  }
  catch (ex) {
    ;
  }
  var rc = null;
  if (!tagName) {
    if (childs.length > 0) {
      return childs[0];
    }
    return rc;
  }
  for (var i = 0; i < childs.length; i = i + 1) {
    if (childs[i].nodeName == tagName) {
      rc = childs[i];
      break;
    }
  }
  return rc;
}

getFirstChildTextNode = function (node) {
  var childs = node.childNodes;
  if (!childs) {
    childs = node.children;
  }
  if (!childs) {
    childs = [];
  }
  var rc = null;
  for (var i = 0; i < childs.length; i = i + 1) {
    if (childs[i].nodeName == "#text") {
      rc = childs[i];
      break;
    }
  }
  return rc;
};
getFirstDownNode = function (node, tagName) {
  try {
    if (node.nodeName == tagName) {
      return node;
    }
  }
  catch (ex) {
    ;
  }
  var rc = null;
  var childs = null;
  try {
    childs = node.childNodes;
    if (!childs) {
      childs = node.children;
    }
    if (!childs) {
      childs = [];
    }
  }
  catch (ex) {
    ;
  }
  if (!childs) {
    childs = [];
  }
  for (var i = 0; i < childs.length; i = i + 1) {
    var child = childs[i];
    if (child.nodeName == tagName) {
      rc = child;
      break;
    }
  }
  if (!rc) {
    for (i = 0; i < childs.length; i = i + 1) {
      child = childs[i];
      rc = getFirstDownNode(child, tagName);
      if (rc) {
        break;
      }
    }
  }
  return rc;
};
//-------------------------------------------
getDownNodes = function (node, tagName, rc) {
  if (!rc) {
    rc = [];
  }
  if (tagName) {
    if (node.nodeName == tagName) {
      rc[rc.length] = node;
    }
  }
  else {
    rc[rc.length] = node;
  }
  var childs = node.childNodes;
  if (!childs) {
    childs = node.children;
  }
  if (!childs) {
    childs = [];
  }
  for (var i = 0; i < childs.length; i = i + 1) {
    var child = childs[i];
    getDownNodes(child, tagName, rc);
  }
  return rc;
};

getDownNodesByTagNameList = function (node, tagNameList, rc) {
  if (!rc) {
    rc = [];
  }
  if (tagNameList) {
		if (tagNameList.indexOf(node.nodeName) > -1  ) {
			rc[rc.length] = node;
		}
  }
  else {
    rc[rc.length] = node;
  }
  var childs = node.childNodes;
  if (!childs) {
    childs = node.children;
  }
  if (!childs) {
    childs = [];
  }
  for (var i = 0; i < childs.length; i = i + 1) {
    var child = childs[i];
    getDownNodesByTagNameList(child, tagNameList, rc);
  }
  return rc;
};


getDownNodesByAttribute = function (node, tagName,attName,attValue) {
	var rc = getDownNodes(node, tagName);
	if ( !rc || rc.length === 0 ) {
		return rc;
	}
	var rcNew = [];
	for(var i=0;i<rc.length;i++) {
		//var value = rc[i].getAttribute(attName);
		var value = jQuery(rc[i]).attr(attName);
		if ( value == attValue ) {
			 rcNew[rcNew.length] = rc[i];
		}
	}	
	return rcNew;
};

getDownNodesByAttributes = function (node, tagName,attNames,attValues) {
	var rc = getDownNodes(node, tagName);
	if ( !rc || rc.length === 0 ) {
		return rc;
	}
	var rcNew = [];
	for(var i=0;i<rc.length;i++) {
	    if (isAttributesTestOk(rc[i],attNames,attValues)) {
	      rcNew[rcNew.length] = rc[i];
	    }
	}	
	return rcNew;
};

getFirstDownNodeByAttributes = function (node, tagName,attNames,attValues) {
	var rc = getDownNodes(node, tagName);
	if ( !rc || rc.length === 0 ) {
		return null;
	}
	for(var i=0;i<rc.length;i++) {
	    if (isAttributesTestOk(rc[i],attNames,attValues)) {
	      return rc[i];
	    }
	}	
	return null;
};

function isAttributesTestOk(cnt,attNames,attValues) {
   	for(var i=0;i<attNames.length;i++) {
        var isNotOk = false;
        try {
          //isNotOk = cnt.getAttribute(attNames[i]) != attValues[i];
          isNotOk = jQuery(cnt).attr(attNames[i]) != attValues[i];
        }
        catch(ex) {
          return false;
        }
        if (isNotOk) {
   	    return false;
   	  }
   	}
   	return true;
}

getChildNodes = function (node, tagName)
{
  var childs = node.childNodes;
  if (!childs) {
    childs = node.children;
  }
  if (!childs) {
    childs = [];
  }

  if (!tagName) {
	  return childs;
  }
  var rcNew = [];
  for (var i = 0; i < childs.length; i++) {
    if (childs[i].nodeName == tagName) {
      rcNew[rcNew.length] = childs[i];
    }
  }
  return rcNew;
};



getChildNodesByAttribute = function (node, tagName,attName,attValue) {
	var rc = getChildNodes(node, tagName);
	if ( !rc || rc.length === 0 ) {
		return rc;
	}
	var rcNew = [];
	for(var i=0;i<rc.length;i++) {
		/*
		if ( !rc[i].getAttribute ) {
			continue;
		}
		*/
		//var value = rc[i].getAttribute(attName);
		var value = jQuery(rc[i]).attr(attName);
		if ( value == attValue ) {
			 rcNew[rcNew.length] = rc[i];
		}
	}	
	return rcNew;
};



removeNodes = function(list) {
  if ( !list ) {
    return;
  }
  for(var i=0;i<list.length;i++) {
    list[i].parentNode.removeChild(list[i]);
  }
};

removeDownNodes = function(parent,tag) {
  var list = getDownNodes(parent,tag);
  removeNodes(list);
};

getAllDownNodes = function (node, rc) {
  //window._syserror=0;
  if (!rc) {
    rc = [];
  }
  if ( !node ) {
    return rc;
  }
  rc[rc.length] = node;
  var childs = null;
  try {
    childs = node.childNodes;
  }
  catch(ex) {
    return rc;
  }
  if (!childs) {
    childs = node.children;
  }
  if (!childs) {
    childs = [];
  }
  for (var i = 0; i < childs.length; i = i + 1) {
    var child = childs[i];
    getAllDownNodes(child, rc);
  }
  return rc;
};

//--------------
getElementListById = function (parent, id) {
  var rc = [];
  var all = getAllDownNodes(parent);
  for(var i = 0; i < all.length; i=i+1) {
    var item = all[i];
    var id_item = item.id;
    if (id == id_item) {
      rc[rc.length] = item;
    }
  }
  return rc;
};
getFirstElementById = function (parent, id) {
  var rc = null;
  var all = getAllDownNodes(parent);
  for(var i = 0; i < all.length; i=i+1) {
    var item = all[i];
    var id_item = item.id;
    if (id == id_item) {
      rc = item;
      break;
    }
  }
  return rc;
};

getFirstElementByPartId = function (parent, id) {
  var rc = null;
  var all = getAllDownNodes(parent);
  for(var i = 0; i < all.length; i=i+1) {
    var item = all[i];
    var id_item = item.id;
    //if (id == id_item) {
    if (id_item && id_item.indexOf(id) > -1 ) {
      rc = item;
      break;
    }
  }
  return rc;
};

/*
findElementAdfPageById = function (id) {
  id=":"+id;
  var rc = null;
  var all = getAllDownNodes(document);
  for(var i = 0; i < all.length; i=i+1) {
    var item = all[i];
    var id_item = item.id;
    if (id_item && id_item.indexOf(id) > 0 ) {
      rc = item;
      break;
    }
  }
  return rc;
};
*/
findElementAdfPageById = function (id) {
  id=":"+id;
  var rc = null;
  jQuery("*").each(
    function(n) 
    {
      var id_item = this.id;
      if (id_item && id_item.indexOf(id) > 0 ) {
        rc = this;
        return false; // break;
      }
      return true; // continue;
    }
  );
  return rc;
}
findElementAdfPageByIdAndTagName = function (id,tagName) {
  id=":"+id;
  var rc = null;
  jQuery("*").find(tagName).each(
    function(n) 
    {
      var id_item = this.id;
      if (id_item && id_item.indexOf(id) > 0 ) {
        rc = this;
        return false; // break;
      }
      return true; // continue;
    }
  );
  return rc;
}
//*** Tree End
function debugTrace(txt) {
  var inp = document.createElement("<INPUT>");
  inp.value=txt;
  document.body.appendChild(inp);
}