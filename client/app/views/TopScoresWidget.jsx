import React from 'react';
import AltContainer from 'alt-container';

import VertxActions from '../actions/VertxActions';
import TopGamesStore from '../stores/TopGamesStore';
import VertxStore from '../stores/VertxStore';

var gridItem = {
  display: "inline-block",
  border: "2px solid black",
  width: "28px",
  height: "28px",
  margin: "2px",
  textAlign: "center",
  fontSize: "1.1em"
};

var grid = {
  display: "block",
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

class BoardWrapper extends React.Component {

  render() {
    return (
      <div style={grid}>
        {this.props.data.tiles.map(function (result, idx) {
          return <TileWrapper key={idx} data={result}/>;
        })}
      </div>
    );
  }
}

class WidgetView extends React.Component {

  render() {
    return (
      <div>
        {this.props.server.boards.map(function (result, idx) {
          return <BoardWrapper key={idx} data={result}/>;
        })}
      </div>
    );
  }
}

export default class TopScoresWidget extends React.Component {
  static getStores(props) {
    return [GameStateStore]
  }

  static getPropsFromStores(props) {
    return GameStateStore.getState()
  }

  render() {
    return (
      <div className="ui segment">
        <AltContainer stores={{vertx: VertxStore, server: TopGamesStore}}>
          <WidgetView />
        </AltContainer>
      </div>
    );
  }
}
