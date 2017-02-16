Type.registerNamespace("Sys.Extended.UI");Sys.Extended.UI.TimeSpan=function(){if(arguments.length==0)this._ctor$0.apply(this,arguments);else if(arguments.length==1)this._ctor$1.apply(this,arguments);else if(arguments.length==3)this._ctor$2.apply(this,arguments);else if(arguments.length==4)this._ctor$3.apply(this,arguments);else if(arguments.length==5)this._ctor$4.apply(this,arguments);else throw Error.parameterCount();};Sys.Extended.UI.TimeSpan.prototype={_ctor$0:function(){this._ticks=0},_ctor$1:function(n){this._ctor$0();this._ticks=n},_ctor$2:function(n,t,i){this._ctor$0();this._ticks=n*Sys.Extended.UI.TimeSpan.TicksPerHour+t*Sys.Extended.UI.TimeSpan.TicksPerMinute+i*Sys.Extended.UI.TimeSpan.TicksPerSecond},_ctor$3:function(n,t,i,r){this._ctor$0();this._ticks=n*Sys.Extended.UI.TimeSpan.TicksPerDay+t*Sys.Extended.UI.TimeSpan.TicksPerHour+i*Sys.Extended.UI.TimeSpan.TicksPerMinute+r*Sys.Extended.UI.TimeSpan.TicksPerSecond},_ctor$4:function(n,t,i,r,u){this._ctor$0();this._ticks=n*Sys.Extended.UI.TimeSpan.TicksPerDay+t*Sys.Extended.UI.TimeSpan.TicksPerHour+i*Sys.Extended.UI.TimeSpan.TicksPerMinute+r*Sys.Extended.UI.TimeSpan.TicksPerSecond+u*Sys.Extended.UI.TimeSpan.TicksPerMillisecond},getDays:function(){return Math.floor(this._ticks/Sys.Extended.UI.TimeSpan.TicksPerDay)},getHours:function(){return Math.floor(this._ticks/Sys.Extended.UI.TimeSpan.TicksPerHour)%24},getMinutes:function(){return Math.floor(this._ticks/Sys.Extended.UI.TimeSpan.TicksPerMinute)%60},getSeconds:function(){return Math.floor(this._ticks/Sys.Extended.UI.TimeSpan.TicksPerSecond)%60},getMilliseconds:function(){return Math.floor(this._ticks/Sys.Extended.UI.TimeSpan.TicksPerMillisecond)%1e3},getDuration:function(){return new Sys.Extended.UI.TimeSpan(Math.abs(this._ticks))},getTicks:function(){return this._ticks},getTotalDays:function(){Math.floor(this._ticks/Sys.Extended.UI.TimeSpan.TicksPerDay)},getTotalHours:function(){return Math.floor(this._ticks/Sys.Extended.UI.TimeSpan.TicksPerHour)},getTotalMinutes:function(){return Math.floor(this._ticks/Sys.Extended.UI.TimeSpan.TicksPerMinute)},getTotalSeconds:function(){return Math.floor(this._ticks/Sys.Extended.UI.TimeSpan.TicksPerSecond)},getTotalMilliseconds:function(){return Math.floor(this._ticks/Sys.Extended.UI.TimeSpan.TicksPerMillisecond)},add:function(n){return new Sys.Extended.UI.TimeSpan(this._ticks+n.getTicks())},subtract:function(n){return new Sys.Extended.UI.TimeSpan(this._ticks-n.getTicks())},negate:function(){return new Sys.Extended.UI.TimeSpan(-this._ticks)},equals:function(n){return this._ticks==n.getTicks()},compareTo:function(n){return this._ticks>n.getTicks()?1:this._ticks<n.getTicks()?-1:0},toString:function(){return this.format("F")},format:function(n){var f,t;if(n||(n="F"),n.length==1)switch(n){case"t":n=Sys.Extended.UI.TimeSpan.ShortTimeSpanPattern;break;case"T":n=Sys.Extended.UI.TimeSpan.LongTimeSpanPattern;break;case"F":n=Sys.Extended.UI.TimeSpan.FullTimeSpanPattern;break;default:throw Error.createError(String.format(Sys.Extended.UI.Resources.Common_DateTime_InvalidTimeSpan,n));}var u=/dd|d|hh|h|mm|m|ss|s|nnnn|nnn|nn|n/g,i=new Sys.StringBuilder,r=this._ticks;for(r<0&&(i.append("-"),r=-r);;){if(f=u.lastIndex,t=u.exec(n),i.append(n.slice(f,t?t.index:n.length)),!t)break;switch(t[0]){case"dd":case"d":i.append($common.padLeft(Math.floor(r/Sys.Extended.UI.TimeSpan.TicksPerDay,t[0].length,"0")));break;case"hh":case"h":i.append($common.padLeft(Math.floor(r/Sys.Extended.UI.TimeSpan.TicksPerHour)%24,t[0].length,"0"));break;case"mm":case"m":i.append($common.padLeft(Math.floor(r/Sys.Extended.UI.TimeSpan.TicksPerMinute)%60,t[0].length,"0"));break;case"ss":case"s":i.append($common.padLeft(Math.floor(r/Sys.Extended.UI.TimeSpan.TicksPerSecond)%60,t[0].length,"0"));break;case"nnnn":case"nnn":case"nn":case"n":i.append($common.padRight(Math.floor(r/Sys.Extended.UI.TimeSpan.TicksPerMillisecond)%1e3,t[0].length,"0",!0));break;default:Sys.Debug.assert(!1)}}return i.toString()}};Sys.Extended.UI.TimeSpan.parse=function(n){var t=n.split(":"),s=0,u=0,f=0,r=0,e=0,o=0,i;switch(t.length){case 1:t[0].indexOf(".")!=-1?(i=t[0].split("."),r=parseInt(i[0]),e=parseInt(i[1])):o=parseInt(t[0]);break;case 2:u=parseInt(t[0]);f=parseInt(t[1]);break;case 3:u=parseInt(t[0]);f=parseInt(t[1]);t[2].indexOf(".")!=-1?(i=t[2].split("."),r=parseInt(i[0]),e=parseInt(i[1])):r=parseInt(t[2]);break;case 4:s=parseInt(t[0]);u=parseInt(t[1]);f=parseInt(t[2]);t[3].indexOf(".")!=-1?(i=t[3].split("."),r=parseInt(i[0]),e=parseInt(i[1])):r=parseInt(t[3])}if(o+=s*Sys.Extended.UI.TimeSpan.TicksPerDay+u*Sys.Extended.UI.TimeSpan.TicksPerHour+f*Sys.Extended.UI.TimeSpan.TicksPerMinute+r*Sys.Extended.UI.TimeSpan.TicksPerSecond+e*Sys.Extended.UI.TimeSpan.TicksPerMillisecond,!isNaN(o))return new Sys.Extended.UI.TimeSpan(o);throw Error.create(Sys.Extended.UI.Resources.Common_DateTime_InvalidFormat);};Sys.Extended.UI.TimeSpan.fromTicks=function(n){return new Sys.Extended.UI.TimeSpan(n)};Sys.Extended.UI.TimeSpan.fromDays=function(n){return new Sys.Extended.UI.TimeSpan(n*Sys.Extended.UI.TimeSpan.TicksPerDay)};Sys.Extended.UI.TimeSpan.fromHours=function(n){return new Sys.Extended.UI.TimeSpan(n*Sys.Extended.UI.TimeSpan.TicksPerHour)};Sys.Extended.UI.TimeSpan.fromMinutes=function(n){return new Sys.Extended.UI.TimeSpan(n*Sys.Extended.UI.TimeSpan.TicksPerMinute)};Sys.Extended.UI.TimeSpan.fromSeconds=function(){return new Sys.Extended.UI.TimeSpan(minutes*Sys.Extended.UI.TimeSpan.TicksPerSecond)};Sys.Extended.UI.TimeSpan.fromMilliseconds=function(){return new Sys.Extended.UI.TimeSpan(minutes*Sys.Extended.UI.TimeSpan.TicksPerMillisecond)};Sys.Extended.UI.TimeSpan.TicksPerDay=864e9;Sys.Extended.UI.TimeSpan.TicksPerHour=36e9;Sys.Extended.UI.TimeSpan.TicksPerMinute=6e8;Sys.Extended.UI.TimeSpan.TicksPerSecond=1e7;Sys.Extended.UI.TimeSpan.TicksPerMillisecond=1e4;Sys.Extended.UI.TimeSpan.FullTimeSpanPattern="dd:hh:mm:ss.nnnn";Sys.Extended.UI.TimeSpan.ShortTimeSpanPattern="hh:mm";Sys.Extended.UI.TimeSpan.LongTimeSpanPattern="hh:mm:ss";Date.prototype.getTimeOfDay=function(){return new Sys.Extended.UI.TimeSpan(0,this.getHours(),this.getMinutes(),this.getSeconds(),this.getMilliseconds())};Date.prototype.getDateOnly=function(){var n=new Date(this.getFullYear(),this.getMonth(),this.getDate());return this.getMonth()===n.getMonth()&&this.getDate()===n.getDate()||n.setMinutes(120),n};Date.prototype.add=function(n){return new Date(this.getTime()+n.getTotalMilliseconds())};Date.prototype.subtract=function(n){return this.add(n.negate())};Date.prototype.getTicks=function(){return this.getTime()*Sys.Extended.UI.TimeSpan.TicksPerMillisecond};Sys.Extended.UI.FirstDayOfWeek=function(){};Sys.Extended.UI.FirstDayOfWeek.prototype={Sunday:0,Monday:1,Tuesday:2,Wednesday:3,Thursday:4,Friday:5,Saturday:6,Default:7};Sys.Extended.UI.FirstDayOfWeek.registerEnum("Sys.Extended.UI.FirstDayOfWeek");