import Style from "./ColorBox.module.css";

interface Props {
  color: string;
}

const ColorBox = ({ color }: Props) => {
  return (
    <div
      className={Style.colorBox}
      style={{
        backgroundColor: `hsl(${Number.parseFloat(color) * 360}, 50%, 55%)`,
      }}
    ></div>
  );
};

export default ColorBox;
