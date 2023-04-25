import {css, customElement, html, LitElement, property, PropertyValues, state, query} from 'lit-element';
import '@polymer/paper-dialog/paper-dialog.js';
import '@vaadin/vaadin-button/vaadin-button.js';
import '@vaadin/vaadin-item/vaadin-item.js';
import {PaperDialogElement} from "@polymer/paper-dialog";
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@polymer/iron-icon/iron-icon.js';
import {IronIconElement} from "@polymer/iron-icon";
import '@vaadin/vaadin-icons/vaadin-icons.js';

const moment = require("moment");

@customElement('notification-menu')
export class NotificationMenu extends LitElement {

    @property({type: String}) width = "auto";
    @property({type: String}) height = "270px";
    @property({type: String}) orientation = 'left';
    @property({type: String}) icon = 'vaadin:bell';
    @property({type: Boolean}) enableIconAnimation = false;
    @property({type: Boolean}) closeOnClick = true;
    @property({type: Boolean}) autoMarkAllAsRead = true;
    @property({type: Number}) _unread = 0;
    @property({type: Number}) maxItemCount = 99;
    @property({type: String}) maxItemCountLabel = "+99";

    @state()
    private i18n: NotificationI18N = {title: 'Notifications', captionViewAll: 'View all', captionMarkAllAsRead: 'Mark all as read', dateTimeFormatPattern: 'YYYY/MM/DD HH:mm'};

    @state()
    private notifications: NotificationItem[] = [];

    @query('#dialog')
    _dialog!: PaperDialogElement;

    @query('#icon')
    _icon!: IronIconElement;

    static get styles() {
        return [css`
        
            :host {
                --notification-menu-type-unknown: var(--lumo-shade-70pct);
                --notification-menu-type-info: var(--lumo-primary-color);
                --notification-menu-type-success: var(--lumo-success-color);
                --notification-menu-type-warning: #f0ad4e;
                --notification-menu-type-danger: var(--lumo-error-color);
                --notification-menu-badge-background-color: var(--lumo-error-color);
                --notification-menu-badge-color: white;
                --notification-menu-unread-background-color: var(--lumo-shade-5pct);
            }
        
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
            overflow: overlay;
            webkit-overflow-scrolling: var(--layout-scroll_-_-webkit-overflow-scrolling);
            overflow: var(--layout-scroll_-_overflow);
            scrollbar-width: none;
            -ms-overflow-style: none;
          }
          .menu-scrollable::-webkit-scrollbar {
             display: none;
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
            padding-left: 5px;
          }
          .menu-item:hover{
            background-color: var(--lumo-shade-10pct) !important;
          }
          .menu-item[data-read="false"] {
            background-color: var(--notification-menu-unread-background-color);
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
            font-size: 11px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
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
          }
          .menu-item-footer span {cursor: pointer;}
          .menu-item-footer .menu-header:first-child{flex: 1 0 auto;padding: 0;}
          .menu-item-footer .menu-header:nth-child(2){text-align: end;padding: 0;}
          
          .unknown{border-left-color: var(--notification-menu-type-unknown, --lumo-shade-70pct);}
          .info{border-left-color: var(--notification-menu-type-info, --lumo-primary-color);}
          .success{border-left-color: var(--notification-menu-type-success, --lumo-success-color);}
          .warning{border-left-color: var(--notification-menu-type-warning, #f0ad4e);}
          .danger{border-left-color: var(--notification-menu-type-danger, --lumo-error-color);}
          [hidden]{visibility:hidden;}
          
          .badge {
            width: 22px;
            height: 22px;
            position: absolute;
            top: 0;
            right: -3px;
            border-radius: 20px;
            background: var(--notification-menu-badge-background-color, --lumo-error-color);
            color: var(--notification-menu-badge-color, white);
            cursor: pointer;
            font-size: 10px;
            padding: 0px;
            -webkit-font-smoothing: subpixel-antialiased;
            line-height: 23px;
            display: inline-block;
          }

          @-webkit-keyframes animation {
            0%,25%,83%,100% {-webkit-transform: rotatez(0deg);}
            32.5%,62.5% {-webkit-transform: rotatez(-5deg);}
            47.5%,75.5% {-webkit-transform: rotatez(5deg);}
          }

          @-moz-keyframes animation {
            0%,25%,83%,100% {-moz-transform: rotatez(0deg);}
            32.5%,62.5% {-moz-transform: rotatez(-5deg);}
            47.5%,75.5% {-moz-transform: rotatez(5deg);}
          }

          @keyframes animation {
            0%,25%,83%,100% {transform: rotatez(0deg);}
            32.5%,62.5% {transform: rotatez(-5deg);}
            47.5%,75.5% {transform: rotatez(5deg);}
          }

          .icon{padding: 2px;}
          .animation {
            -webkit-animation: animation 5s ease infinite;
            -moz-animation: animation 5s ease infinite;
            animation: animation 5s ease infinite;
            transform-origin: 50% 0%;
          }
      `];
    }

    render() {
        return html`
            <div>
                <vaadin-button theme="icon tertiary" @click="${this._onButtonClick}">
                    <iron-icon id="icon" class="icon" icon="${this.icon}" slot="prefix"></iron-icon>
                    <span class="badge" slot="suffix"
                          ?hidden="${this._unread < 1}">${this._unread > this.maxItemCount ? this.maxItemCountLabel : this._unread}</span>
                </vaadin-button>
                <paper-dialog id="dialog" class="dialog" no-overlap horizontal-align="${this.orientation}"
                              vertical-align="top">
                    <h4 id="notification-header" class="menu-header">${this.i18n.title}</h4>
                    <paper-dialog-scrollable class="menu-scrollable">
                        <div id="content" class="content" style="width:${this.width};height: ${this.height}">
                            ${this.notifications.map(item => html`
                                <vaadin-item data-read="${item.read}" class="menu-item ${item.type === null ? 'unknown' : item.type}"
                                             @click="${() => this._onItemClicked(item)}">
                                    <div class="menu-item-header">
                                        <span class="title" title="${item.title}"><strong>${item.title}</strong></span>
                                        <span class="datetime" title="${moment(item.datetime).format(this.i18n.dateTimeFormatPattern)}">${moment(item.datetime).format(this.i18n.dateTimeFormatPattern)}</span>
                                    </div>
                                    <div class="description">${item.description}</div>
                                </vaadin-item>
                            `)}
                        </div>
                    </paper-dialog-scrollable>
                    <div class="menu-item-footer">
                        <h4 id="view-all" class="menu-header"><span @click="${this._onViewAll}">${this.i18n.captionViewAll}</span></h4>
                        <h4 id="mark-all-as-read"class="menu-header"><span @click="${this._onMarkAllAsRead}">${this.i18n.captionMarkAllAsRead}</span></h4>
                    </div>
                </paper-dialog>
            </div>
        `;
    }

    protected updated(_changedProperties: PropertyValues) {
        super.updated(_changedProperties);
        this._calculateUnread(this.notifications);
    }

    addItems(items: NotificationItem | NotificationItem[]) {
        items = NotificationMenu.asArray(items);
        let map = NotificationMenu.asMap(this.notifications);

        // add only items that are not yet known to this instance.
        this.notifications.push(...(items.filter(item => !map.has(item.key))));

        this.requestUpdate("notifications");
    }

    updateItems(items: NotificationItem | NotificationItem[]) {
        let map = NotificationMenu.asMap(items);

        for (let i = 0; i < this.notifications.length; i++) {
            let key = this.notifications[i].key;
            let updatedItem = map.get(key);
            if (updatedItem) {
                this.notifications[i] = updatedItem;
            }
        }

        this.requestUpdate("notifications");
    }

    removeItems(items: NotificationItem | NotificationItem[]) {
        let map = NotificationMenu.asMap(items);

        this.notifications = this.notifications.filter(item => !map.has(item.key));

        this.requestUpdate("notifications");
    }

    private static asArray(items: NotificationItem | NotificationItem[]) {
        if (!Array.isArray(items)) {
            items = [items];
        }
        return items;
    }

    private static asMap(items: NotificationItem | NotificationItem[]) {
        items = NotificationMenu.asArray(items);

        let map = new Map<string, NotificationItem>();
        for (let item of items) {
            map.set(item.key, item);
        }

        return map;
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

    /**
     * Marks all items as read. Does not trigger any event.
     */
    markAllAsRead() {
        this.notifications.forEach(item => item.read = true);
        this.requestUpdate("notifications");
    }

    updateI18n(i18n: NotificationI18N){
        this.i18n = Object.assign({},this.i18n,i18n);
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
        if (this.autoMarkAllAsRead) {
            this.markAllAsRead();
        }
        this.close();
        this.dispatchEvent(new CustomEvent('mark-all-clicked'));
    }

    _enableAnimation() {
        if (this.enableIconAnimation) {
            this._icon.classList.add('animation');
            setTimeout(() => this._disableAnimation(), 30000);
        }
    }

    _disableAnimation() {
        this._icon.classList.remove('animation');
    }

    _calculateUnread(notifications: NotificationItem[]) {
        this._unread = notifications.filter(value => value.read === false).length;
        if (this._unread > 0) {
            this._enableAnimation();
        }
    }
}

if (customElements.get('notification-menu') === undefined) {
    customElements.define('notification-menu', NotificationMenu);
}

export interface NotificationItem {
    key: string;
    type?: string;
    title?: string;
    description?: string;
    datetime?: Date;
    read?: Boolean;
}

export interface NotificationI18N {
    title: string;
    captionViewAll: string;
    captionMarkAllAsRead: string;
    dateTimeFormatPattern: string;
}