import { ReactNode } from "react";
import Style from "./SidePanel.module.css";

interface Props {
  children: ReactNode;
}
const SidePanel = ({ children }: Props) => {
  return <article className={Style.sidePanel}>{children}</article>;
};

export default SidePanel;
