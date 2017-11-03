/**
 * 司信JS-SDK V1.0.0
 * Created by iPhuan on 16/3/25.
 * Build 20161017
 */


(function () {
    var JSSDK_BUILD_VERSION = '1.0.0';

    var PLUGIN_METHOD_CONFIGREQUEST = 'configRequest';
    var RESCODE_NEED_WAIT_CONFIGREQUEST_CALLBACK = '40002';
    var RESMSG_NEED_WAIT_CONFIGREQUEST_CALLBACK = 'The request config has not been completed, call \'startRequest\' after \'configRequest\' get success callback';

    var sdkType = {
        SXSdkTypeForDevelopment: 0,
        SXSdkTypeForNative: 1,
        SXSdkTypeForNetwork: 2
    };

    var jsSdkType = sdkType.SXSdkTypeForDevelopment;

    var userAgent = navigator.userAgent;
    var isAndroid = userAgent.indexOf('Android') > -1;
    var isIos = userAgent.indexOf('iPhone') > -1 || userAgent.indexOf('iPad') > -1 || userAgent.indexOf('iPod') > -1;

    function includeCordova() {
        var head = document.getElementsByTagName('head')[0];
        var script = document.createElement('script');
        script.type = 'text/javascript';

        var allLoadJs = document.scripts;
        var sdkJs = allLoadJs[allLoadJs.length - 1];
        var cordovaJsPath = sdkJs.src.substring(0, sdkJs.src.indexOf("common")) + 'cordova/cordova.js';

        if (jsSdkType == sdkType.SXSdkTypeForNetwork) {
//            if (isAndroid) {
                cordovaJsPath = 'file://WebContent/pad/cordova/cordova.js';
//            } else if (isIos) {
//                cordovaJsPath = 'http://jssdk.issmobile.com/JS-SDK/cordova/ios/cordova.js';
//            }
        }

        script.src = cordovaJsPath;
        head.appendChild(script);
    }

    function includeDebugSDK() {
        var head = document.getElementsByTagName('head')[0];


        var debugJsPath = 'http://jssdk.issmobile.com/JS-SDK/debug/jsixin-debug-' + JSSDK_BUILD_VERSION + '.js';
        if (jsSdkType == sdkType.SXSdkTypeForDevelopment) {
            debugJsPath = '../debug/jsixin-debug-' + JSSDK_BUILD_VERSION + '.js';
        }

        var debugScript = document.createElement('script');
        debugScript.type = 'text/javascript';
        debugScript.src = debugJsPath;
        head.appendChild(debugScript);

        var md5JsPath = 'http://jssdk.issmobile.com/JS-SDK/debug/JQuery.md5.js';
        if (jsSdkType == sdkType.SXSdkTypeForDevelopment) {
            md5JsPath = '../debug/JQuery.md5.js';
        }

        var md5Script = document.createElement('script');
        md5Script.type = 'text/javascript';
        md5Script.src = md5JsPath;
        head.appendChild(md5Script);
    }

    function isPc() {
        var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
        var isMac = (navigator.platform == "Mac68K") || (navigator.platform == "MacPPC") || (navigator.platform == "Macintosh") || (navigator.platform == "MacIntel");
        var isUnix = (navigator.platform == "X11");
        var isLinux = (String(navigator.platform).indexOf("Linux") > -1) && !isAndroid;
        return isWin || isMac || isUnix || isLinux;
    }

    var isPc = isPc();

    if (isPc) {
        includeDebugSDK();
        window.addEventListener("load", onLoad, false)
    } else {
        includeCordova();
    }

    document.addEventListener('deviceready', onDeviceReady, false);

    var listenerMethod = function () {
        this.deviceReady = undefined;
        this.resume = undefined;
        this.pause = undefined;
        this.back = undefined;
        this.menu = undefined;
    };

    function onLoad() {
        if (isPc && typeof utils != 'undefined') {
            utils.handleResult = handleResult;
        }
        onDeviceReady()
    }

    function onDeviceReady() {
        document.addEventListener('resume', onResume, false);
        document.addEventListener('pause', onPause, false);

        //only for android
        document.addEventListener('backbutton', onBack, false);
        document.addEventListener('menubutton', onMenu, false);


        if (listenerMethod.deviceReady) {
            listenerMethod.deviceReady();
        }
    }


    function onResume() {
        if (listenerMethod.resume) {
            listenerMethod.resume();
        }
    }

    function onPause() {
        if (listenerMethod.pause) {
            listenerMethod.pause();
        }
    }

    function onBack() {
        if (listenerMethod.back) {
            listenerMethod.back();
        }
    }

    function onMenu() {
        if (listenerMethod.menu) {
            listenerMethod.menu();
        }
    }


    var config = function () {
        this.ready = undefined;
        this.error = undefined;
    };

    function configReady(res) {
        if (config.ready) {
            config.ready(res);
        }
    }

    function configError(err) {
        if (config.error) {
            config.error(err);
        }
    }

    var isConfigRequestCompleted = true;

    var jSixin = {
        version: JSSDK_BUILD_VERSION,
        isDebug: isPc,

        deviceReady: function (obj) {
            listenerMethod.deviceReady = obj;
        },
        appWillEnterForeground: function (obj) {
            listenerMethod.resume = obj;
        },
        appDidEnterBackground: function (obj) {
            listenerMethod.pause = obj;
        },
        clickOnBackButton: function (obj) {
            listenerMethod.back = obj;
        },
        clickOnMenuButton: function (obj) {
            listenerMethod.menu = obj;
        },


        config: function (params) {
            if (isPc) {
                if (typeof debugData == 'undefined') {
                    return;
                }

                var res = debugData.config(params);
                alertDebugResult(res);
                if (res.resCode == '0') {
                    configReady(res);
                } else {
                    configError(res)
                }

                return;
            }

            var array = [];
            if (params != undefined) {
                array = [params];
            }

            cordova.exec(function (res) {
                configReady(res);
            }, function (err) {
                configError(err)
            }, 'SXConfigPlugin', 'config', array);
        },


        ready: function (obj) {
            config.ready = obj;
        },
        error: function (obj) {
            config.error = obj;
        },


        checkPluginApi: function (obj) {
            callCordova('SXBasicPlugin', 'checkPluginApi', obj);
        },
        onDebug: function (obj) {
            callCordova('SXBasicPlugin', 'onDebug', obj);
        },
        closeDebug: function (obj) {
            callCordova('SXBasicPlugin', 'closeDebug', obj);
        },
        getPluginSDKVersion: function (obj) {
            callCordova('SXBasicPlugin', 'getPluginSDKVersion', obj);
        },


        configRequest: function (obj) {
            isConfigRequestCompleted = isPc;
            callCordova('SXNetworkPlugin', 'configRequest', obj);
        },
        startRequest: function (obj) {
            if (!isConfigRequestCompleted) {
                var err = {
                    resCode: RESCODE_NEED_WAIT_CONFIGREQUEST_CALLBACK,
                    resMsg: RESMSG_NEED_WAIT_CONFIGREQUEST_CALLBACK
                };
                handleResult(obj, err);
                return;
            }
            callCordova('SXNetworkPlugin', 'startRequest', obj);
        },
        cancelRequest: function (obj) {
            callCordova('SXNetworkPlugin', 'cancelRequest', obj);
        },


        share: function (obj) {
            callCordova('SXSharePlugin', 'share', obj);
        },
        shareCustomContent: function (obj) {
            callCordova('SXSharePlugin', 'shareCustomContent', obj);
        },
        onMenuShareCustomContent: function (obj) {
            callCordova('SXSharePlugin', 'onMenuShareCustomContent', obj);
        },
        closeMenuShareCustomContent: function (obj) {
            callCordova('SXSharePlugin', 'closeMenuShareCustomContent', obj);
        },


        chooseImage: function (obj) {
            callCordova('SXImagePlugin', 'chooseImage', obj);
        },
        chooseAvatarImage: function (obj) {
            callCordova('SXImagePlugin', 'chooseAvatarImage', obj);
        },
        previewImage: function (obj) {
            callCordova('SXImagePlugin', 'previewImage', obj);
        },
        uploadImage: function (obj) {
            callCordova('SXImagePlugin', 'uploadImage', obj);
        },
        uploadImages: function (obj) {
            callCordova('SXImagePlugin', 'uploadImages', obj);
        },
        downloadImage: function (obj) {
            callCordova('SXImagePlugin', 'downloadImage', obj);
        },
        downloadImages: function (obj) {
            callCordova('SXImagePlugin', 'downloadImages', obj);
        },


        onRecord: function (obj) {
            callCordova('SXAudioPlugin', 'onRecord', obj);
        },
        stopRecord: function (obj) {
            callCordova('SXAudioPlugin', 'stopRecord', obj);
        },
        onPlayVoice: function (obj) {
            callCordova('SXAudioPlugin', 'onPlayVoice', obj);
        },
        pauseVoice: function (obj) {
            callCordova('SXAudioPlugin', 'pauseVoice', obj);
        },
        stopVoice: function (obj) {
            callCordova('SXAudioPlugin', 'stopVoice', obj);
        },

        uploadVoice: function (obj) {
            callCordova('SXAudioPlugin', 'uploadVoice', obj);
        },
        downloadVoice: function (obj) {
            callCordova('SXAudioPlugin', 'downloadVoice', obj);
        },


        getLocation: function (obj) {
            callCordova('SXLocationPlugin', 'getLocation', obj);
        },
        openLocation: function (obj) {
            callCordova('SXLocationPlugin', 'openLocation', obj);
        },


        onShake: function (obj) {
            callCordova('SXShakePlugin', 'onShake', obj);
        },
        closeShake: function (obj) {
            callCordova('SXShakePlugin', 'closeShake', obj);
        },


        scanQRCode: function (obj) {
            callCordova('SXQRCodePlugin', 'scanQRCode', obj);
        },
        generateQRCode: function (obj) {
            callCordova('SXQRCodePlugin', 'generateQRCode', obj);
        },


        changeNavTitle: function (obj) {
            callCordova('SXInterfaceOperationPlugin', 'changeNavTitle', obj);
        },
        hideOptionMenu: function (obj) {
            callCordova('SXInterfaceOperationPlugin', 'hideOptionMenu', obj);
        },
        showOptionMenu: function (obj) {
            callCordova('SXInterfaceOperationPlugin', 'showOptionMenu', obj);
        },
        hideMenuItems: function (obj) {
            callCordova('SXInterfaceOperationPlugin', 'hideMenuItems', obj);
        },
        showMenuItems: function (obj) {
            callCordova('SXInterfaceOperationPlugin', 'showMenuItems', obj);
        },
        showAllMenuItems: function (obj) {
            callCordova('SXInterfaceOperationPlugin', 'showAllMenuItems', obj);
        },
        closeWindow: function (obj) {
            callCordova('SXInterfaceOperationPlugin', 'closeWindow', obj);
        },
        alertMessage: function (obj) {
            callCordova('SXInterfaceOperationPlugin', 'alertMessage', obj);
        },
        showMessage: function (obj) {
            callCordova('SXInterfaceOperationPlugin', 'showMessage', obj);
        },
        showLoading: function (obj) {
            callCordova('SXInterfaceOperationPlugin', 'showLoading', obj);
        },
        hideLoading: function (obj) {
            callCordova('SXInterfaceOperationPlugin', 'hideLoading', obj);
        },


        checkIfUserFollowInstitution: function (obj) {
            callCordova('SXInstitutionPlugin', 'checkIfUserFollowInstitution', obj);
        },
        viewInstitutionInfo: function (obj) {
            callCordova('SXInstitutionPlugin', 'viewInstitutionInfo', obj);
        },
        sendMsgToInstitution: function (obj) {
            callCordova('SXInstitutionPlugin', 'sendMsgToInstitution', obj);
        },


        getUserInfo: function (obj) {
            callCordova('SXUserInfoPlugin', 'getUserInfo', obj);
        },
        getUserRecentContacts: function (obj) {
            callCordova('SXUserInfoPlugin', 'getUserRecentContacts', obj);
        },
        getEmployeeInfo: function (obj) {
            callCordova('SXUserInfoPlugin', 'getEmployeeInfo', obj);
        },
        chooseContacts: function (obj) {
            callCordova('SXUserInfoPlugin', 'chooseContacts', obj);
        },
        chooseOrganization: function (obj) {
            callCordova('SXUserInfoPlugin', 'chooseOrganization', obj);
        },
        getOrganizationInfo: function (obj) {
            callCordova('SXUserInfoPlugin', 'getOrganizationInfo', obj);
        },
        viewEmployeeInfo: function (obj) {
            callCordova('SXUserInfoPlugin', 'viewEmployeeInfo', obj);
        },
        sendMsgToEmployee: function (obj) {
            callCordova('SXUserInfoPlugin', 'sendMsgToEmployee', obj);
        },


        getDeviceNetworkType: function (obj) {
            callCordova('SXDevicePlugin', 'getDeviceNetworkType', obj);
        },
        getDeviceSystemType: function (obj) {
            callCordova('SXDevicePlugin', 'getDeviceSystemType', obj);
        },
        getDeviceSystemVersion: function (obj) {
            callCordova('SXDevicePlugin', 'getDeviceSystemVersion', obj);
        },
        getDeviceModel: function (obj) {
            callCordova('SXDevicePlugin', 'getDeviceModel', obj);
        },
        getDeviceUniqueID: function (obj) {
            callCordova('SXDevicePlugin', 'getDeviceUniqueID', obj);
        },
        getDeviceInfo: function (obj) {
            callCordova('SXDevicePlugin', 'getDeviceInfo', obj);
        },


        backupData: function (obj) {
            callCordova('SXLocalStoragePlugin', 'backupData', obj);
        },
        restoreData: function (obj) {
            callCordova('SXLocalStoragePlugin', 'restoreData', obj);
        },
        removeData: function (obj) {
            callCordova('SXLocalStoragePlugin', 'removeData', obj);
        },
        removeAllData: function (obj) {
            callCordova('SXLocalStoragePlugin', 'removeAllData', obj);
        },


        pay: function (obj) {
            callCordova('SXPayPlugin', 'pay', obj);
        },


        getAppVersion: function (obj) {
            callCordova('SXAdditionsPlugin', 'getAppVersion', obj);
        }
    };


    var resultType = {
        success: 0,
        enable: 1,
        trigger: 2,
        cancel: 3,
        refuse: 4,
        fail: 5
    };


    function callCordova(pluginName, methodName, obj) {

        if (isPc) {
            if (typeof sendDebugResult != 'undefined'){
                sendDebugResult(pluginName, methodName, obj);
            }
            return;
        }

        var array = [];
        if (obj != undefined && obj.params != undefined) {
            array = [obj.params];
        }
        cordova.exec(function (res) {
            if (methodName == PLUGIN_METHOD_CONFIGREQUEST) {
                isConfigRequestCompleted = true;
            }
            handleResult(obj, res);
        }, function (err) {
            if (methodName == PLUGIN_METHOD_CONFIGREQUEST) {
                isConfigRequestCompleted = true;
            }
            handleResult(obj, err);
        }, pluginName, methodName, array);
    }


    var handleResult = function (obj, res) {
        var dic = res;
        if (typeof res === 'string') {
            dic = JSON.parse(res);
        }

        var callbackType = resultType.success;
        if (res != undefined && res.resCode) {
            callbackType = parseInt(res.resCode);
            if (callbackType > resultType.refuse) {
                callbackType = resultType.fail;
            }
        }

        sendResult(obj, res, callbackType);
    };


    function sendResult(obj, result, type) {

        if (!obj) {
            return;
        }

        var needCompleteMethod = true;
        switch (type) {
            case resultType.success:
                if (obj.success) {
                    obj.success(result);
                }
                break;
            case resultType.enable:
                if (obj.enable) {
                    obj.enable(result);
                }
                needCompleteMethod = false;
                break;
            case resultType.trigger:
                if (obj.trigger) {
                    obj.trigger(result);
                }
                needCompleteMethod = false;
                break;
            case resultType.cancel:
                if (obj.cancel) {
                    obj.cancel(result);
                }
                break;
            case resultType.refuse:
                if (obj.refuse) {
                    obj.refuse(result);
                }
                break;
            case resultType.fail: 
                if (obj.fail) {
                    obj.fail(result);
                }
                break;
            default:
                break;
        }

        if (needCompleteMethod) {
            if (obj.complete) {
                obj.complete(result);
            }
        }
    }
    window.sx = jSixin;
})();
