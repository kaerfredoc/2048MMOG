import React from 'react';

var gridItem = {
  display: "inline-block",
  border: "2px solid black",
  width: "38px",
  height: "38px",
  margin: "2px",
  textAlign: "center",
  fontSize: "1.1em"
};

var grid = {
  display: "inline-block",
  textAlign: "center",
  padding: "10px"
};

class TileWrapper extends React.Component {

  render() {
    return (
      <div style={gridItem}>
        { (this.props.data === 0) ? <span>&nbsp;</span> : this.props.data }
      </div>
    );
  }
}

export default class BoardWrapper extends React.Component {

  render() {
    return (
      <div style={grid}>
        <div>{this.props.data.name} [{this.props.data.score}]</div>
        <div>
          {this.props.data.tiles.slice(0, 4).map(function (result, idx) {
            return <TileWrapper key={idx} data={result}/>;
          })}
        </div>
        <div>
          {this.props.data.tiles.slice(4, 8).map(function (result, idx) {
            return <TileWrapper key={idx} data={result}/>;
          })}
        </div>
        <div>
          {this.props.data.tiles.slice(8, 12).map(function (result, idx) {
            return <TileWrapper key={idx} data={result}/>;
          })}
        </div>
        <div>
          {this.props.data.tiles.slice(12, 16).map(function (result, idx) {
            return <TileWrapper key={idx} data={result}/>;
          })}
        </div>
      </div>
    );
  }
}