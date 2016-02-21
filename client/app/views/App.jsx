import React from 'react';
import AltContainer from 'alt-container';
import BoardWidget from './BoardWidget';
import VertxStore from '../stores/VertxStore.js';
import VertxActions from '../actions/VertxActions';

export default class App extends React.Component {

  render() {
    return (
  <div className="container">
    <div className="">
      <BoardWidget/>
    </div>
    <div className="heading">
      <h1 className="title">2048</h1>
      <div className="scores-container">
        <div className="score-container">0</div>
        <div className="best-container">0</div>
      </div>
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

      <div className="grid-container">
        <div className="grid-row">
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
        </div>
        <div className="grid-row">
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
        </div>
        <div className="grid-row">
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
        </div>
        <div className="grid-row">
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
          <div className="grid-cell"></div>
        </div>
      </div>

      <div className="tile-container">

      </div>
    </div>

    <p className="game-explanation">
      <strong className="important">How to play:</strong> Use your <strong>arrow keys</strong> to move the tiles. When two tiles with the same number touch, they <strong>merge into one!</strong>
    </p>
  </div>
    );
  }

}