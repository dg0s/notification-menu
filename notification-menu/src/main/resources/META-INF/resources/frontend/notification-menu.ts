import {css, customElement, html, LitElement, property, PropertyValues, state} from 'lit-element';
import '@polymer/paper-dialog/paper-dialog.js';
import '@vaadin/vaadin-ordered-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/vaadin-button/src/vaadin-button.js';
import '@vaadin/vaadin-item/vaadin-item.js';
import '@polymer/iron-icon/iron-icon.js';
import {query} from 'lit-element/lib/decorators.js';
import {PaperDialogElement} from "@polymer/paper-dialog";
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import {IronIconElement} from "@polymer/iron-icon";

const moment = require("moment");

@customElement('notification-menu')
export class NotificationMenu extends LitElement {

    @property({type: String}) width = "auto";
    @property({type: String}) height = "270px";
    @property({type: String}) orientation = 'left';
    @property({type: String}) title = 'Notifications';
    @property({type: Boolean}) ringBell = false;
    @property({type: Boolean}) closeOnClick = true;
    @property({type: Number}) _unread = 0;

    @state()
    private notifications: NotificationItem[] = [];

    @query('#dialog')
    _dialog!: PaperDialogElement;

    @query('#bell')
    _bell!: IronIconElement;

    static get styles() {
        return [css`
          .dialog{
            margin: 0px 25px;
            min-width: 250px;
          }
          .menu-scrollable{
            display: block;
            border-top: 1px solid var(--lumo-contrast-5pct);
            border-bottom: 1px solid var(--lumo-contrast-5pct);
            margin:0;
            padding: 0 15px;
          }
          .menu-item{
            min-width: 0px;
            display: block;
            font-size: var(--lumo-font-size-s);
            line-height: var(--lumo-line-height-m);
            margin-bottom: 10px;
            border: 1px solid var(--lumo-contrast-5pct);
            border-left-width: .25rem;
            border-radius: var(--lumo-border-radius);
          }
          .menu-item:hover{
            background-color: var(--lumo-shade-10pct) !important;
          }
          .menu-item[data-read="false"] {
            background-color: var(--lumo-shade-5pct);
          }
          .menu-item-header{
            display: flex;
          }
          .menu-item-header .title{
            width: 60%;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
          .menu-item-header .datetime{
            width: 40%;
            text-align: end;
            font-size: 12px;
          }
          .menu-item .description{
            width: 99%;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
          }
          .content{
            height: 270px;
            padding-top: 10px;
            overflow: overlay;          
          }
          .menu-action-view{
            width: 100%;
            margin-bottom: 14px;
            margin-top: 14px;
            padding: 0 15px;
          }
          .menu-header{
            margin-block-start: 1em;
            margin-block-end: 1em;
            padding: 0 15px;
          }
          .menu-item-footer{
            display:flex;
            margin: 0;
            padding: 0 15px;
            color: var(--lumo-primary-text-color);
            cursor: pointer;
          }
          .menu-item-footer .menu-header:first-child{flex: 1 0 auto;padding: 0;}
          .menu-item-footer .menu-header:nth-child(2){text-align: end;padding: 0;}
          
          .unknown{border-left-color: var(--lumo-shade-70pct);}
          .info{border-left-color: var(--lumo-primary-color);}
          .success{border-left-color: var(--lumo-success-color);}
          .warning{border-left-color: #f0ad4e;}
          .danger{border-left-color: var(--lumo-error-color);}
          [hidden]{visibility:hidden;}
          
          .badge {
            width: 22px;
            height: 22px;
            position: absolute;
            top: 0;
            right: -3px;
            border-radius: 20px;
            background: var(--lumo-error-color);
            color: white;
            cursor: pointer;
            font-size: 10px;
            padding: 0px;
            -webkit-font-smoothing: subpixel-antialiased;
            line-height: 23px;
            display: inline-block;
          }

          @-webkit-keyframes bell-shake {
            0%,25%,83%,100% {-webkit-transform: rotatez(0deg);}
            32.5%,62.5% {-webkit-transform: rotatez(-5deg);}
            47.5%,75.5% {-webkit-transform: rotatez(5deg);}
          }

          @-moz-keyframes bell-shake {
            0%,25%,83%,100% {-moz-transform: rotatez(0deg);}
            32.5%,62.5% {-moz-transform: rotatez(-5deg);}
            47.5%,75.5% {-moz-transform: rotatez(5deg);}
          }

          @keyframes bell-shake {
            0%,25%,83%,100% {transform: rotatez(0deg);}
            32.5%,62.5% {transform: rotatez(-5deg);}
            47.5%,75.5% {transform: rotatez(5deg);}
          }

          .bell{padding: 2px;}
          .bell-shake {
            -webkit-animation: bell-shake 5s ease infinite;
            -moz-animation: bell-shake 5s ease infinite;
            animation: bell-shake 5s ease infinite;
            transform-origin: 50% 0%;
          }
      `];
    }

    render() {
        return html`
            <div>
                <vaadin-button theme="icon tertiary" @click="${this._onButtonClick}">
                    <iron-icon id="bell" class="bell" icon="vaadin:bell" slot="prefix"></iron-icon>
                    <span class="badge" slot="suffix"
                          ?hidden="${this._unread < 1}">${this._unread > 99 ? '+99' : this._unread}</span>
                </vaadin-button>
                <paper-dialog id="dialog" class="dialog" no-overlap horizontal-align="${this.orientation}"
                              vertical-align="top">
                    <h4 class="menu-header">${this.title}</h4>
                    <paper-dialog-scrollable class="menu-scrollable">
                        <div id="content" class="content" style="width:${this.width};height: ${this.height}">
                            ${this.notifications.map(item => html`
                                <vaadin-item data-read="${item.read}" class="menu-item ${item.type === null ? 'unknown' : item.type}"
                                             @click="${() => this._onItemClicked(item)}">
                                    <div class="menu-item-header">
                                        <span class="title" title="${item.title}"><strong>${item.title}</strong></span>
                                        <span class="datetime">${moment(item.datetime).format('YYYY/MM/DD HH:mm')}</span>
                                    </div>
                                    <div class="description">${item.description}</div>
                                </vaadin-item>
                            `)}
                        </div>
                    </paper-dialog-scrollable>
                    <div class="menu-item-footer">
                        <h4 @click="${this._onViewAll}" id="view-all" class="menu-header">View all</h4>
                        <h4 @click="${this._onMarkAllAsRead}" class="menu-header">Mark all as read</h4>
                    </div>
                </paper-dialog>
            </div>
        `;
    }

    protected updated(_changedProperties: PropertyValues) {
        super.updated(_changedProperties);
        this._calculateUnread(this.notifications);
    }

    open() {
        if (this._dialog != null && !this._dialog.opened) {
            this._dialog.open();
        }
    }

    close() {
        if (this._dialog != null && this._dialog.opened) {
            this._dialog.close();
        }
    }

    _onButtonClick() {
        this.open();
    }

    _onItemClicked(item: NotificationItem) {
        if (this.closeOnClick) {
            this.close();
        }
        this.dispatchEvent(new CustomEvent('item-clicked', {
            detail: item
        }));
    }

    _onViewAll() {
        this.close();
        this.dispatchEvent(new CustomEvent('view-all-clicked'));
    }

    _onMarkAllAsRead() {
        this.close();
        this.dispatchEvent(new CustomEvent('mark-all-clicked'));
    }

    _ringBell() {
        if (this.ringBell) {
            this._bell.classList.add('bell-shake');
            setTimeout(() => this._muteBell(), 30000);
        }
    }

    _muteBell() {
        this._bell.classList.remove('bell-shake');
    }

    _calculateUnread(notifications: NotificationItem[]) {
        this._unread = notifications.filter(value => value.read === false).length;
        if (this._unread > 0) {
            this._ringBell();
        }
    }
}

export class NotificationItem {
    key?: String;
    type?: String;
    title?: String;
    description?: String;
    datetime?: Date;
    read?: Boolean;
}