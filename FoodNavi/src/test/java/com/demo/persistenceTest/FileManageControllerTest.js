const $ = require('jquery');
const jsdom = require('jsdom');
const { JSDOM } = jsdom;
const dom = new JSDOM();
global.window = dom.window;
global.$ = global.jQuery = require('jquery')(window);
const jqueryMockjax = require('jquery-mockjax')(global.jQuery, window);

// Set up our document body
document.body.innerHTML =
  '<div id="editor"></div>';

global.$ = global.jQuery = $;
$.summernote = { 'insertImage': jest.fn() };

describe('uploadSummernoteImageFile', () => {
  it('calls insertImage on successful AJAX request', () => {
    // Mock AJAX request
    jqueryMockjax($, {
      url: '/uploadSummernoteImageFile',
      responseText: {
        url: 'http://example.com/image.jpg'
      }
    });

    // Call the function with a mock file and editor
    const file = new Blob(['file contents'], { type: 'text/plain' });
    const editor = $('#editor');
    uploadSummernoteImageFile(file, editor);

    // Check that insertImage was called
    expect($.summernote.insertImage).toHaveBeenCalledWith('http://example.com/image.jpg');
  });
});