var webpack = require("webpack");
var path = require("path");
const ExtractTextPlugin = require("extract-text-webpack-plugin");
const VueLoaderPlugin = require('vue-loader/lib/plugin');

module.exports = {
	entry : {
		"common/base" : './assets/entry/common/base.js', // 基础平台的 js //
															// 也可以定义为数组、分开
	  "home/index" : './assets/entry/home/index.js', // css and js

	//暂时不动	"common/ngEdit" : './assets/entry/common/ngEdit.js',// angular 版本页面引入
		"common/gridList" : './assets/entry/common/gridList.js', // list页面
	//	"common/codeMirror" : './assets/entry/common/codeMirror.js', // codeMirror
		"common/vueEdit" : './assets/entry/common/vueEdit.js' // vue 版本的引入
	},
	output : {
		path : path.resolve(__dirname, './build/'),
		filename : '[name].js',// -v[hash:8]
	},
	module : {
		rules : [
				{
					test : /\.vue$/,
					loader : 'vue-loader'
				},
				{
					test : /\.scss$/,
					use : [ 'style-loader', 'css-loader', 'sass-loader' ]
				},
				{
					test : /\.css$/,
					loader : ExtractTextPlugin.extract({
						fallback : "style-loader",
						use : [ {
							loader : "css-loader",
							options : {
								minimize : true // css 压缩
							}
						}]
					})
				},
				{
					test : /\.(png|jpg|gif)$/,
					loader : 'url-loader?limit=8192&name=../images/[name]-[hash:8].[ext]'
				}, {
					test : /\.(woff|woff2|ttf|eot|svg|)$/,
					loader : "url-loader?limit=5000&name=../font/[name].[ext]"
				}, {
					test : require.resolve("jquery"),
					loader : "expose-loader?$!expose-loader?jQuery"
				} ]
	},
	devtool : false,// 'eval-source-map',// 开发环境配置。其他环境请设置为false
	plugins : [ 
				new webpack.BannerPlugin('AGILE-BPM 版权所有，翻版必究'),
				new ExtractTextPlugin("[name].css"), new VueLoaderPlugin() ],
	devServer : {
		inline : true,
	},
	// 我们使用 webpack 的 vue-loader 对模板进行渲染，所以要使用 runtime版本的 Vue
	 resolve: {
	        alias: {
	            vue: 'vue/dist/vue.js',
	        }
	 }

}