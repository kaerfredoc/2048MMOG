import React from 'react';
import AltContainer from 'alt-container';

import VertxActions from '../actions/VertxActions';
import GameStateStore from '../stores/GameStateStore';
import VertxStore from '../stores/VertxStore';
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

class BoardView extends React.Component {

  render() {
    if (this.props.server && this.props.server.board) {
      return (
      <div className="">
        <div className="heading">
          <h1 className="title">2048</h1>
        </div>

        <div className="above-game">
          <p className="game-intro">Join the numbers and get to the <strong>2048 tile!</strong></p>
          <a className="restart-button">New Game</a>
        </div>

        <div className="game-container">
          <div className="game-message">
            <p></p>
            <div className="lower">
              <a className="keep-playing-button">Keep going</a>
              <a className="retry-button">Try again</a>
            </div>
          </div>

          <BoardWrapper style={style} data={this.props.server.board}/>
        </div>

        <p className="game-explanation">
          <strong className="important">How to play:</strong> Use your <strong>arrow keys</strong> to move the tiles.
          When two tiles with the same number touch, they <strong>merge into one!</strong>
        </p>
      </div>
    )} else {
      return (<div/>);
    }
  }
}

export default class BoardWidget extends React.Component {

  render() { return (
      <div className="row">
        <AltContainer stores={{vertx: VertxStore, server: GameStateStore}}>
          <BoardView />
        </AltContainer>
      </div>
  )}
}
