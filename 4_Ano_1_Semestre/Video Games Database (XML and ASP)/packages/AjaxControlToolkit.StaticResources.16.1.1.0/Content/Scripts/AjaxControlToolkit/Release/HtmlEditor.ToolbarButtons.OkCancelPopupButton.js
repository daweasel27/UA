﻿Type.registerNamespace("Sys.Extended.UI.HtmlEditor.ToolbarButtons");Sys.Extended.UI.HtmlEditor.ToolbarButtons.OkCancelPopupButton=function(n){Sys.Extended.UI.HtmlEditor.ToolbarButtons.OkCancelPopupButton.initializeBase(this,[n])};Sys.Extended.UI.HtmlEditor.ToolbarButtons.OkCancelPopupButton.prototype={set_activeEditPanel:function(n){var t,i;this._editPanel!=n&&this._editPanel!=null&&(t=this.get_relatedPopup(),typeof t._forceImClose=="function"&&(i=t._forceImClose,i(t._iframe.contentWindow)));Sys.Extended.UI.HtmlEditor.ToolbarButtons.DesignModePopupImageButton.callBaseMethod(this,"set_activeEditPanel",[n])},callMethod:function(){return Sys.Extended.UI.HtmlEditor.ToolbarButtons.OkCancelPopupButton.callBaseMethod(this,"callMethod")?(this.openPopup(Function.createDelegate(this,this._onopened)),!0):!1},_onopened:function(n){n.popupMediator.set_callMethodByName("OK",Function.createDelegate(this,this._onOK));n.popupMediator.set_callMethodByName("Cancel",Function.createDelegate(this,this._onCancel));var t=this.get_relatedPopup();t._popup=this._designPanel._popup;t._forceImClose=Function.createDelegate(this,this._onCancel);this._designPanel._popup=this.get_relatedPopup();this.opened(n)},opened:function(){},_onOK:function(n){this.okCheck(n)&&this._exit(Function.createDelegate(this,this.ok),n)},_onCancel:function(n){this.cancelCheck(n)&&this._exit(Function.createDelegate(this,this.cancel),n)},_exit:function(n,t){this.closePopup();this._designPanel._popup=this.get_relatedPopup()._popup;this.get_relatedPopup()._popup=null;this.get_relatedPopup()._forceImClose=null;n(t)},ok:function(){},cancel:function(){},okCheck:function(){return!0},cancelCheck:function(){return!0}};Sys.Extended.UI.HtmlEditor.ToolbarButtons.OkCancelPopupButton.registerClass("Sys.Extended.UI.HtmlEditor.ToolbarButtons.OkCancelPopupButton",Sys.Extended.UI.HtmlEditor.ToolbarButtons.DesignModePopupImageButton);