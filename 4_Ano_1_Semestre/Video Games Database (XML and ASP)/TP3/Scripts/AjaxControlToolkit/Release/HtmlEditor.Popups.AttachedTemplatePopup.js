﻿Type.registerNamespace("Sys.Extended.UI.HtmlEditor.Popups");Sys.Extended.UI.HtmlEditor.Popups.AttachedTemplatePopup=function(n){Sys.Extended.UI.HtmlEditor.Popups.AttachedTemplatePopup.initializeBase(this,[n]);this._contentDiv=null};Sys.Extended.UI.HtmlEditor.Popups.AttachedTemplatePopup.prototype={get_contentDiv:function(){return this._contentDiv},set_contentDiv:function(n){this._contentDiv=n},initialize:function(){Sys.Extended.UI.HtmlEditor.Popups.AttachedTemplatePopup.callBaseMethod(this,"initialize");this.set_initialContent(this._contentDiv.innerHTML)},dispose:function(){Sys.Extended.UI.HtmlEditor.Popups.AttachedTemplatePopup.callBaseMethod(this,"dispose")}};Sys.Extended.UI.HtmlEditor.Popups.AttachedTemplatePopup.registerClass("Sys.Extended.UI.HtmlEditor.Popups.AttachedTemplatePopup",Sys.Extended.UI.HtmlEditor.Popups.AttachedPopup);