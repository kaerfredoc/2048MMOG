import React from 'react';
import { createForm } from 'rc-form';
import VertxActions from '../actions/VertxActions';
import GameStateStore from '../stores/GameStateStore';

@createForm()
export default class RegistrationForm extends React.Component {
  submit = () => {
    VertxActions.register(this.props.form.getFieldValue('normal'));
  };

  render() {
    const {getFieldProps, getFieldError} = this.props.form;
    return (<div className="row">
      <input {...getFieldProps('normal')}/>
      <button onClick={this.submit}>Join Contest</button>
    </div>)
  }
}