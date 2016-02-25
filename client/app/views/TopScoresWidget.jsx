import React from 'react';
import AltContainer from 'alt-container';

import VertxActions from '../actions/VertxActions';
import TopGamesStore from '../stores/TopGamesStore';
import VertxStore from '../stores/VertxStore';
import AutoResponsive from 'autoresponsive-react';
import BoardWrapper from './BoardWrapper';

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

var grid = {
  display: "inline-block",
  textAlign: "center",
  padding: "10px"
};

class TopScoresView extends React.Component {
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

  render() {
    return (
      <div className="row">
        <AltContainer stores={{vertx: VertxStore, server: TopGamesStore}}>
          <TopScoresView />
        </AltContainer>
      </div>
    );
  }
}
