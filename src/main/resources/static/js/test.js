const rootElement = document.getElementById('root')
class TitleComponent extends React.Component {
    render() {
        return (
            <h1> {this.props.title}</h1>
        );
    }
}

function App(){
  return(
    <div>
        <TitleComponent title="React Stranger Things"/>
    </div>
  )
}

ReactDOM.render(
    <App />,
    rootElement
)