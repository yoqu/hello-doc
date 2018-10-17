var $ = require('jquery');
require('@/styles/md.css')
import hljs from 'highlight.js';
import marked from 'marked';
import flowchart from 'flowchart.js/release/flowchart'; // ugly
let flowchartOptions = {
    x: 0,
    y: 0,
    fill: 'white',
    scale: 1,
    'line-width': 2,
    'line-length': 40,
    'text-margin': 10,
    'font-size': 14,
    'font-color': 'black',
    'line-color': 'grey',
    'element-color': 'grey',
    'yes-text': 'yes',
    'no-text': 'no',
    'arrow-end': 'block',
    flowstate: {
        past: { fill: '#CCCCCC', 'font-size': 12 },
        current: { fill: 'yellow', 'font-color': 'red', 'font-weight': 'bold' },
        future: { fill: '#FFFF99' },
        request: { fill: 'blue' },
        invalid: { fill: '#444444', 'font-color': 'white' },
        approved: { fill: '#58C4A3', 'font-size': 12, 'yes-text': 'APPROVED', 'no-text': 'n/a' },
        rejected: { fill: '#C45879', 'font-size': 12, 'yes-text': 'n/a', 'no-text': 'REJECTED' },
    },
};
let flowchartInstanceCache = [];
let Renderer = marked.Renderer;
let RendererPrototype = Renderer.prototype;
let renderer = new Renderer();
marked.__scriptsToLoad = [];
marked.__linksToLoad = [];
marked.__jsCodeToLoad = '';
marked.__cssCodeToLoad = '';
renderer.code = function (code, language, escaped, lineNumber) { // code block
    code = $.trim(code);
    // flowchart
    if (language === 'flowchart' || language === 'flow') {
        code = code.replace(/^\n/, '').split(/\n/).map(function (line) {
            // have to trim
            return $.trim(line);
        }).join('\n');
        return '<div class="flowchart"><script  type="text/template" class="flowchart-code">' + code + '</script><div class="flowchart-graph"></div></div>';
    }
    return RendererPrototype.code.apply(this, arguments);
};
let options = {
    figcaption: true,
    breaks: false,
    pedantic: false,
    renderer: renderer,
    sanitize: false,
    smartLists: true,
    smartypants: true,
    tables: true,
};
marked.setOptions(options);
function renderFlowcharts(scope) {
    setTimeout(function(){
        $(scope).find(".flowchart").each(function ( index,container) {
            try {
                var codeElement = $(container).children('.flowchart-code');
                var graphElement = $(container).children('.flowchart-graph');
                var diagram = flowchart.parse(codeElement[0].innerText);
                diagram.drawSVG(graphElement[0]);
            } catch (e) {
                console.error(e);
            }
        })
    },0)
}
export default {
    render: function (container, markdownString = '') {
        container.innerHTML = marked(markdownString);
        $(container).find('pre code').each(function (index,block) {                
            hljs.highlightBlock(block);
        })
        renderFlowcharts(container);
    }
}
