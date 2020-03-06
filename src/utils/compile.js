import marked from 'marked';
import flowchart from 'flowchart.js';
export default function (newOptions) {
    let Renderer = marked.Renderer;
    let RendererPrototype = Renderer.prototype;
    let renderer = new Renderer();
    marked.__scriptsToLoad = [];
    marked.__linksToLoad = [];
    marked.__jsCodeToLoad = '';
    marked.__cssCodeToLoad = '';
    renderer.code = function (code, language, escaped, lineNumber) { // code block
        code = Object.trim(code);
        // flowchart
        if (language === 'flowchart') {
            code = Object.map(code.replace(/^\n/, '').split(/\n/), function (line) {
                // have to trim
                return Object.trim(line);
            }).join('\n');
            return '<div class="flowchart"><div class="flowchart-code">'+code+'</script><div class="flowchart-graph"></div></div>';
        }
        return RendererPrototype.code.apply(this, arguments);
    };
    marked.setOptions(newOptions);
    return marked;
};
