var path = require('path');

module.exports = {
	entry: './src/js/src/index.js',
	devtool: 'sourcemaps',
	cache: true,
	mode: 'development',
	output: {
		path: __dirname,
		filename: './src/main/resources/static/built/bundle.js'
	},
	module: {
		rules: [
			{
				test: path.join(__dirname, '.'),
				exclude: /(node_modules)/,
				use: [{
					loader: 'babel-loader',
					options: {
						presets: ["@babel/preset-env", "@babel/preset-react",
						{'plugins': ['@babel/plugin-proposal-class-properties']}]
					}
				}]
			},
					
			{ test: /\.(png|woff|woff2|eot|ttf|svg)$/, loader: 'url-loader?limit=100000' },
			{
				test: /\.css$/,
				use: [
					'style-loader',
					'css-loader'
				]
			}
		],
	}
};