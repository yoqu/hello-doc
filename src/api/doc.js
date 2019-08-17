import request from '@/utils/request'
import $ from 'jquery';

function getBaseUrl() {
    if (localStorage.getItem("config") != null) {
        let baseUrl = JSON.parse(localStorage.getItem("config"))['baseUrl'];
        return fixBaseUrl(baseUrl)
    } else {
        return ''
    }
}

function getMdBaseUrl(){
    if(localStorage.getItem("config")!=null){
        let mdUrl = JSON.parse(localStorage.getItem("config"))['mdUrl'];
        return fixBaseUrl(mdUrl)
    }else{
        return ''
    }
}

export function fixBaseUrl(baseUrl) {
    if(baseUrl===undefined || baseUrl===''){
        return''
    }
    if (baseUrl.lastIndexOf('/') !== baseUrl.length - 1) {
        baseUrl = baseUrl + '/'
    }
    if (!/^(https?|ftp|file):\/\/.+$/.test(baseUrl)) {
        return '//' + baseUrl
    }
    return baseUrl
}

export function getBasicDefinition(cb) {
    $.ajax({
        url: getBaseUrl() + 'static/data/basic_definition.json',
        type: 'get',
        async: false,
        success: function (response) {
            cb(true, response);
        },
        error: function (error) {
            cb(false, error)
        }
    })
}

export function getDtos() {
    return request({
        url: getBaseUrl() + "static/data/dto.json"
    })
}

export function getServices() {
    return request({
        url: getBaseUrl() + 'static/data/service.json'
    })
}

export function getTimeline() {
    return request({
        url: getBaseUrl() + 'static/data/timelines.json'
    })
}

export function getNotes(doc) {
    return request({
        url: getMdBaseUrl() + 'docs/' + doc + '.md'
    })
}

export function getMenu() {
    return request({
        url: getBaseUrl() + 'static/data/nav_menu.json'
    })
}
