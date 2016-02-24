import React from 'react';
import AltContainer from 'alt-container';

import VertxActions from '../actions/VertxActions';
import TopGamesStore from '../stores/TopGamesStore';
import VertxStore from '../stores/VertxStore';
import AutoResponsive from 'autoresponsive-react';

let style = {
  height: 100,
  width: 100,
  cursor: 'default',
  color: '#514713',
  borderRadius: 5,
  boxShadow: '0 1px 0 rgba(255,255,255,0.5) inset',
  backgroundColor: '#a28f27',
  borderColor: '#796b1d',
  fontSize: '80px',
  lineHeight: '100px',
  textAlign: 'center',
  fontWeight: 'bold',
  textShadow: '1px 1px 0px #ab9a3c'
};

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

class BoardWrapper extends React.Component {

  render() {
    return (
      <div style={grid}>
        <div>{this.props.data.name} [{this.props.data.score}]</div>
        <div>
          {this.props.data.tiles.slice(0,4).map(function (result, idx) {
            return <TileWrapper key={idx} data={result}/>;
          })}
        </div>
        <div>
          {this.props.data.tiles.slice(4,8).map(function (result, idx) {
            return <TileWrapper key={idx} data={result}/>;
          })}
        </div>
        <div>
          {this.props.data.tiles.slice(8,12).map(function (result, idx) {
            return <TileWrapper key={idx} data={result}/>;
          })}
        </div>
        <div>
          {this.props.data.tiles.slice(12,16).map(function (result, idx) {
            return <TileWrapper key={idx} data={result}/>;
          })}
        </div>
      </div>
    );
  }
}

class WidgetView extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      itemMargin: 10,
      horizontalDirection: 'left',
      verticalDirection: 'top',
      containerHeight: null
    };
  }

  getAutoResponsiveProps() {
    return {
      horizontalDirection: this.state.horizontalDirection,
      verticalDirection: this.state.verticalDirection,
      itemMargin: this.state.itemMargin,
      containerWidth: this.state.containerWidth || this.props.containerWidth,
      itemClassName: 'item',
      containerHeight: this.state.containerHeight,
      transitionDuration: '.8',
      transitionTimingFunction: 'easeIn'
    };
  }

  render() {
    return (
    <AutoResponsive style={grid} ref="container" {...this.getAutoResponsiveProps()}>
      {this.props.server.boards.map(function (result, idx) {
        return (
          <BoardWrapper style={style} key={idx} data={result}/>
        )
      })}
    </AutoResponsive>
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
      <div className="row">
        <AltContainer stores={{vertx: VertxStore, server: TopGamesStore}}>
          <WidgetView />
        </AltContainer>
      </div>
    );
  }
}
